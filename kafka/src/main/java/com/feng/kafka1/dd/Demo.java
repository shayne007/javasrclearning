package com.feng.kafka1.dd;

import org.apache.kafka.clients.consumer.KafkaConsumer;

import com.feng.kafka1.dd.multithreads.KafkaConsumerRunner;

/**
 * @author fengsy
 * @date 3/26/21
 * @Description
 */
public class Demo {
    public static void main(String[] args) {
        // sendMsg();
        consumeMsg();

    }

    private static void consumeMsg() {
        String topic = "topic-test";
        String groupid = "group-test";
        String brokerlist = "127.0.0.1:9092";
        KafkaConsumer<String, String> consumer = KafkaConsumerUtil.initConsumerParams(brokerlist, topic, groupid, null);
        new Thread(new KafkaConsumerRunner(consumer)).start();
    }

    private static void sendMsg() {
        String topic = "topic-test";
        String value = "msg string value";
        String groupid = "group-test";
        String brokerlist = "127.0.0.1:9092";
        for (int i = 0; i < 100; i++) {
            KafkaProducerUtil.sendMessage(topic, groupid, value, brokerlist);
        }
    }
}
