package com.feng.order.client.handler.dispatcher;

import com.feng.order.common.ResponseMessage;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author fengsy
 * @date 8/4/21
 * @Description
 */
public class ResponseDispatcherHandler extends SimpleChannelInboundHandler<ResponseMessage> {
    private RequestPendingCenter requestPendingCenter;

    public ResponseDispatcherHandler(RequestPendingCenter requestPendingCenter) {
        this.requestPendingCenter = requestPendingCenter;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ResponseMessage msg) throws Exception {
        requestPendingCenter.set(msg.getHeader().getStreamId(), msg.getBody());
    }
}
