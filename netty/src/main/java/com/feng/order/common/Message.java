package com.feng.order.common;

import java.nio.charset.StandardCharsets;

import com.feng.order.util.JsonUtil;

import io.netty.buffer.ByteBuf;
import lombok.Data;

/**
 * @author fengsy
 * @date 8/3/21
 * @Description
 */
@Data
public abstract class Message<T extends MessageBody> {
    private MessageHeader header;
    private T body;

    public T getBody() {
        return body;
    }

    public void encode(ByteBuf buf) {
        buf.writeInt(header.getVersion());
        buf.writeInt(header.getOpCode());
        buf.writeLong(header.getStreamId());
        buf.writeBytes(JsonUtil.toJson(body).getBytes());
    }

    public void decode(ByteBuf msg) {
        int version = msg.readInt();
        int opCode = msg.readInt();
        long streamId = msg.readLong();

        MessageHeader header = new MessageHeader();
        header.setVersion(version);
        header.setOpCode(opCode);
        header.setStreamId(streamId);
        this.header = header;
        Class<T> bodyClazz = getMessageBodyDecodeClass(opCode);
        T body = JsonUtil.fromJson(msg.toString(StandardCharsets.UTF_8), bodyClazz);
        this.body = body;
    }

    public abstract Class<T> getMessageBodyDecodeClass(int opcode);
}
