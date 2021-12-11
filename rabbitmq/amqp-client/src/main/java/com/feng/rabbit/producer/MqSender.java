package com.feng.rabbit.producer;

/**
 * @author fengsy
 * @Description
 * @date 10/30/21
 */
public interface MqSender {
    /**
     * 发送简单消息
     *
     * @param body  消息体
     * @param topic 主题
     * @param group 组
     */
    void sendMessage(String body, String topic, String group);
}
