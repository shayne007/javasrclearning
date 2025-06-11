package com.feng.kafka;

import com.feng.kafka.multithreads.KafkaConsumerRunner;
import com.feng.kafka.multithreads.ThreadPoolKafkaConsumerRunner;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;

/**
 * @author fengsy
 * @date 3/26/21
 * @Description
 */
public class ConsumerDemo {
    public static void main(String[] args) {
        consumeMsg();
    }

    private static void consumeMsg() {
        String topic = "topic-test";
        String groupid = "group-test6";
        String brokerlist = "110.42.251.23:9092";
        // String brokerlist = "127.0.0.1:55000,127.0.0.1:55001";
        // KafkaConsumerUtil.receiveMessage(brokerlist, topic, groupid, 1000L, null);
        KafkaConsumer<String, String> consumer = KafkaConsumerUtil.initConsumerParams(brokerlist, groupid);
        // KafkaConsumerRunner runner = new SingleThreadKafkaConsumerRunner(consumer, Arrays.asList(topic), null);
        KafkaConsumerRunner runner = new ThreadPoolKafkaConsumerRunner(consumer, Arrays.asList(topic), 2000L);
        new Thread(runner).start();
        try {
            Thread.sleep(100000);
            runner.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
