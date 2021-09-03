package com.feng.order.server.codec;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * @author fengsy
 * @date 8/3/21
 * @Description
 */
public class OrderFrameDecoder extends LengthFieldBasedFrameDecoder {

    public OrderFrameDecoder() {
        super(10240, 0, 2, 0, 2);
    }
}
