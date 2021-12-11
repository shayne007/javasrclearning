package com.feng.kafka;

/**
 * @author fengsy
 * @date 3/26/21
 * @Description
 */
public class ProducerDemo {
    public static void main(String[] args) {
        sendMsg();

    }

    private static void sendMsg() {
        String topic = "topic-test";
        String value = "msg string value";
        String brokerlist = "110.42.251.23:9092";
        // String brokerlist = "127.0.0.1:55000,127.0.0.1:55001";
        for (int i = 0; i < 100; i++) {
            KafkaProducerUtil.sendMessage(topic, value, brokerlist);
        }
    }
}
