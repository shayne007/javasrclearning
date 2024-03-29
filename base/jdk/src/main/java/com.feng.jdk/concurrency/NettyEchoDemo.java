package com.feng.jdk.concurrency;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author fengsy
 * @date 1/19/21
 * @Description
 */
public class NettyEchoDemo {
    public static void main(String[] args) throws InterruptedException {

        // 事件处理器
        final EchoServerHandler serverHandler = new EchoServerHandler();
        // boss线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        // worker线程组
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(serverHandler);
                        }
                    });
            // bind服务端端口
            ChannelFuture f = b.bind(9090).sync();
            f.channel().closeFuture().sync();
        } finally {
            // 终止工作线程组
            workerGroup.shutdownGracefully();
            // 终止boss线程组
            bossGroup.shutdownGracefully();
        }

    }
}

// socket连接处理器
class EchoServerHandler extends ChannelInboundHandlerAdapter {
    // 处理读事件
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ctx.write(msg);
    }

    // 处理读完成事件
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    // 处理异常事件
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}