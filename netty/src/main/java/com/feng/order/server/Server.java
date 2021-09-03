package com.feng.order.server;

import java.security.cert.CertificateException;

import javax.net.ssl.SSLException;

import com.feng.order.server.codec.OrderFrameDecoder;
import com.feng.order.server.codec.OrderFrameEncoder;
import com.feng.order.server.codec.OrderProtocolDecoder;
import com.feng.order.server.codec.OrderProtocolEncoder;
import com.feng.order.server.handler.AuthHandler;
import com.feng.order.server.handler.MetricsHandler;
import com.feng.order.server.handler.OrderServerProcessHandler;
import com.feng.order.server.handler.ServerIdleCheckHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioChannelOption;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.flush.FlushConsolidationHandler;
import io.netty.handler.ipfilter.IpFilterRuleType;
import io.netty.handler.ipfilter.IpSubnetFilterRule;
import io.netty.handler.ipfilter.RuleBasedIpFilter;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import io.netty.handler.traffic.GlobalTrafficShapingHandler;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.UnorderedThreadPoolEventExecutor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author fengsy
 * @date 8/3/21
 * @Description
 */
@Slf4j
public class Server {
    private static final int SERVER_PORT = 8090;
    private static final String FILTER_IP = "127.1.1.1";

    public void run() throws InterruptedException, CertificateException, SSLException {
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        serverBootstrap.channel(NioServerSocketChannel.class);
        serverBootstrap.option(NioChannelOption.SO_BACKLOG, 1024);
        serverBootstrap.childOption(NioChannelOption.TCP_NODELAY, true);
        serverBootstrap.childOption(NioChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        serverBootstrap.handler(new LoggingHandler(LogLevel.INFO));

        // reactor thread model
        // Math.max(1, SystemPropertyUtil.getInt("io.netty.eventLoopThreads", NettyRuntime.availableProcessors() * 2));
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(0, new DefaultThreadFactory("boss"));
        NioEventLoopGroup workerGroup = new NioEventLoopGroup(0, new DefaultThreadFactory("worker"));
        UnorderedThreadPoolEventExecutor businessGroup =
            new UnorderedThreadPoolEventExecutor(10, new DefaultThreadFactory("business"));
        NioEventLoopGroup eventLoopGroupForTrafficShaping = new NioEventLoopGroup(0, new DefaultThreadFactory("TS"));

        try {
            serverBootstrap.group(bossGroup, workerGroup);
            // metrics
            MetricsHandler metricsHandler = new MetricsHandler();
            // trafficShaping
            GlobalTrafficShapingHandler globalTrafficShapingHandler =
                new GlobalTrafficShapingHandler(eventLoopGroupForTrafficShaping, 10 * 1024 * 1024, 10 * 1024 * 1024);
            // ipfilter
            IpSubnetFilterRule ipSubnetFilterRule = new IpSubnetFilterRule(FILTER_IP, 16, IpFilterRuleType.REJECT);
            RuleBasedIpFilter ruleBasedIpFilter = new RuleBasedIpFilter(ipSubnetFilterRule);
            // auth
            AuthHandler authHandler = new AuthHandler();

            // ssl
            SelfSignedCertificate selfSignedCertificate = new SelfSignedCertificate();
            log.info("certificate position:" + selfSignedCertificate.certificate().toString());
            SslContext sslContext = SslContextBuilder
                .forServer(selfSignedCertificate.certificate(), selfSignedCertificate.privateKey()).build();

            // log
            LoggingHandler debugLogHandler = new LoggingHandler(LogLevel.DEBUG);
            LoggingHandler infoLogHandler = new LoggingHandler(LogLevel.INFO);
            serverBootstrap.childHandler(new ChannelInitializer<NioServerSocketChannel>() {
                @Override
                protected void initChannel(NioServerSocketChannel ch) throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();
                    pipeline.addLast("debugLog", debugLogHandler);
                    pipeline.addLast("ipFilter", ruleBasedIpFilter);
                    pipeline.addLast("tsHandler", globalTrafficShapingHandler);
                    pipeline.addLast("metricHandler", metricsHandler);
                    pipeline.addLast("idleHandler", new ServerIdleCheckHandler());

                    pipeline.addLast("ssl", sslContext.newHandler(ch.alloc()));

                    pipeline.addLast("frameDecoder", new OrderFrameDecoder());
                    pipeline.addLast("frameEncoder", new OrderFrameEncoder());

                    pipeline.addLast("protocolDecoder", new OrderProtocolDecoder());
                    pipeline.addLast("protocolEncoder", new OrderProtocolEncoder());

                    pipeline.addLast("infolog", infoLogHandler);

                    pipeline.addLast("flushEnhance", new FlushConsolidationHandler(10, true));

                    pipeline.addLast("auth", authHandler);

                    pipeline.addLast(businessGroup, new OrderServerProcessHandler());
                }
            });

            ChannelFuture channelFuture = serverBootstrap.bind(SERVER_PORT).sync();

            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            businessGroup.shutdownGracefully();
            eventLoopGroupForTrafficShaping.shutdownGracefully();
        }

    }

    public static void main(String[] args) throws InterruptedException, SSLException, CertificateException {
        new Server().run();
    }
}
