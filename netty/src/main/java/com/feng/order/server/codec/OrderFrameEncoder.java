package com.feng.order.server.codec;

import io.netty.handler.codec.LengthFieldPrepender;

/**
 * @author fengsy
 * @date 8/3/21
 * @Description
 */
public class OrderFrameEncoder extends LengthFieldPrepender {

    public OrderFrameEncoder() {
        super(2);
    }
}
