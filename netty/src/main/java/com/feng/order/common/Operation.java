package com.feng.order.common;

/**
 * @author fengsy
 * @date 8/3/21
 * @Description
 */
public abstract class Operation extends MessageBody {
    public abstract OperationResult execute();

}
