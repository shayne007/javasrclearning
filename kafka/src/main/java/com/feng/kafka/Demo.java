package com.feng.kafka;

import java.util.Arrays;

import org.apache.kafka.clients.consumer.KafkaConsumer;

import com.feng.kafka.multithreads.KafkaConsumerRunner;
import com.feng.kafka.multithreads.ThreadPoolKafkaConsumerRunner;

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
        String groupid = "group-test6";
        String brokerlist = "127.0.0.1:9092";
        // String brokerlist = "127.0.0.1:55000,127.0.0.1:55001";
        // KafkaConsumerUtil.receiveMessage(brokerlist, topic, groupid, 1000L, null);
        KafkaConsumer<String, String> consumer = KafkaConsumerUtil.initConsumerParams(brokerlist, groupid);
        // KafkaConsumerRunner runner = new SingleThreadKafkaConsumerRunner(consumer, Arrays.asList(topic), null);
        KafkaConsumerRunner runner = new ThreadPoolKafkaConsumerRunner(consumer, Arrays.asList(topic), 2000L);
        new Thread(runner).start();
        try {
            Thread.sleep(10000);
            runner.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private static void sendMsg() {
        String topic = "topic-test";
        String value = "msg string value";
        String brokerlist = "127.0.0.1:9092";
        // String brokerlist = "127.0.0.1:55000,127.0.0.1:55001";
        for (int i = 0; i < 100; i++) {
            KafkaProducerUtil.sendMessage(topic, value, brokerlist);
        }
    }
}
