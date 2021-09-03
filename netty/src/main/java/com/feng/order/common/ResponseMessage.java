package com.feng.order.common;

/**
 * @author fengsy
 * @date 8/3/21
 * @Description
 */
public class ResponseMessage extends Message<OperationResult> {
    @Override
    public Class getMessageBodyDecodeClass(int opcode) {
        return OperationType.fromOpCode(opcode).getOperationResultClazz();
    }
}
