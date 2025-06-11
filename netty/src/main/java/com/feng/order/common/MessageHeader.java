package com.feng.order.common;

import lombok.Data;

/**
 * @author fengsy
 * @date 8/3/21
 * @Description
 */
@Data
public class MessageHeader {
    private int version = 1;
    private int opCode;
    private long streamId;

    public MessageHeader() {}

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getOpCode() {
        return opCode;
    }

    public void setOpCode(int opCode) {
        this.opCode = opCode;
    }

    public long getStreamId() {
        return streamId;
    }

    public void setStreamId(long streamId) {
        this.streamId = streamId;
    }
}
