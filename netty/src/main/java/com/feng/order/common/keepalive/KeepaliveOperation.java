package com.feng.order.common.keepalive;

import com.feng.order.common.Operation;

import lombok.Data;

/**
 * @author fengsy
 * @date 8/3/21
 * @Description
 */
@Data
public class KeepaliveOperation extends Operation {
    private long time;

    public KeepaliveOperation() {
        this.time = System.nanoTime();
    }

    @Override
    public KeepaliveOperationResult execute() {
        KeepaliveOperationResult orderResponse = new KeepaliveOperationResult(time);
        return orderResponse;
    }
}
