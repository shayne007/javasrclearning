package com.feng.order.client.handler;

import io.netty.handler.timeout.IdleStateHandler;

/**
 * @author fengsy
 * @date 8/4/21
 * @Description
 */
public class ClientIdleCheckHandler extends IdleStateHandler {

    public ClientIdleCheckHandler() {
        super(0, 5, 0);
    }
}
