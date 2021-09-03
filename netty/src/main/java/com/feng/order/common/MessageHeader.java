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
}
