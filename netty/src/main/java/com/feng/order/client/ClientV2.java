package com.feng.order.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author fengsy
 * @date 8/3/21
 * @Description
 */
public class ClientV2 {
    public static void main(String[] args) {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.channel(NioSocketChannel.class);
    }
}
