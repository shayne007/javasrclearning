package com.feng.order.common.keepalive;

import com.feng.order.common.OperationResult;

import lombok.Data;

/**
 * @author fengsy
 * @date 8/3/21
 * @Description
 */
@Data
public class KeepaliveOperationResult extends OperationResult {
    private final long time;

    public KeepaliveOperationResult(long time) {
        this.time = time;
    }

    public KeepaliveOperationResult() {
        this.time = 0L;
    }
}
