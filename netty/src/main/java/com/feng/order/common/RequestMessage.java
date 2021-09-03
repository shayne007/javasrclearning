package com.feng.order.common;

/**
 * @author fengsy
 * @date 8/3/21
 * @Description
 */
public class RequestMessage extends Message<Operation> {
    public RequestMessage() {}

    public RequestMessage(Long streamId, Operation operation) {
        MessageHeader messageHeader = new MessageHeader();
        messageHeader.setStreamId(streamId);
        messageHeader.setOpCode(OperationType.fromOperation(operation).getOpCode());
        this.setHeader(messageHeader);
        this.setBody(operation);
    }

    @Override
    public Class getMessageBodyDecodeClass(int opcode) {
        return OperationType.fromOpCode(opcode).getOperationClazz();
    }
}
