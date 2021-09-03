package com.feng.order.client;

import java.util.concurrent.ExecutionException;

import javax.net.ssl.SSLException;

import com.feng.order.client.codec.OperationToRequestMessageEncoder;
import com.feng.order.client.codec.OrderFrameDecoder;
import com.feng.order.client.codec.OrderFrameEncoder;
import com.feng.order.client.codec.OrderProtocolDecoder;
import com.feng.order.client.codec.OrderProtocolEncoder;
import com.feng.order.client.handler.dispatcher.OperationResultFuture;
import com.feng.order.client.handler.dispatcher.RequestPendingCenter;
import com.feng.order.client.handler.dispatcher.ResponseDispatcherHandler;
import com.feng.order.common.OperationResult;
import com.feng.order.common.RequestMessage;
import com.feng.order.common.order.OrderOperation;
import com.feng.order.util.IdUtil;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

/**
 * @author fengsy
 * @date 8/3/21
 * @Description
 */
public class ClientV2 {
    public static void main(String[] args) throws SSLException, InterruptedException, ExecutionException {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.channel(NioSocketChannel.class);

        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            bootstrap.group(group);

            RequestPendingCenter requestPendingCenter = new RequestPendingCenter();

            SslContextBuilder sslContextBuilder = SslContextBuilder.forClient();
            sslContextBuilder.trustManager(InsecureTrustManagerFactory.INSTANCE);
            SslContext sslContext = sslContextBuilder.build();

            bootstrap.handler(new ChannelInitializer<NioSocketChannel>() {
                @Override
                protected void initChannel(NioSocketChannel ch) throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();

                    pipeline.addLast(sslContext.newHandler(ch.alloc()));

                    pipeline.addLast(new OrderFrameDecoder());
                    pipeline.addLast(new OrderFrameEncoder());
                    pipeline.addLast(new OrderProtocolEncoder());
                    pipeline.addLast(new OrderProtocolDecoder());

                    pipeline.addLast(new ResponseDispatcherHandler(requestPendingCenter));

                    pipeline.addLast(new OperationToRequestMessageEncoder());

                    pipeline.addLast(new LoggingHandler(LogLevel.INFO));
                }
            });

            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8090);

            channelFuture.sync();

            long streamId = IdUtil.nextId();

            RequestMessage requestMessage = new RequestMessage(streamId, new OrderOperation(1001, "tudou"));

            OperationResultFuture operationResultFuture = new OperationResultFuture();

            requestPendingCenter.add(streamId, operationResultFuture);

            channelFuture.channel().writeAndFlush(requestMessage);

            OperationResult operationResult = operationResultFuture.get();

            System.out.println(operationResult);

            channelFuture.channel().closeFuture().sync();

        } finally {
            group.shutdownGracefully();
        }
    }
}
