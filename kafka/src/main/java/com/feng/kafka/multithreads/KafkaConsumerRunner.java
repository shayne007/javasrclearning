package com.feng.kafka.multithreads;

import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;

/**
 * @author fengsy
 * @date 3/23/21
 * @Description
 */

public class KafkaConsumerRunner implements Runnable {
    private final AtomicBoolean closed = new AtomicBoolean(false);
    private final KafkaConsumer consumer;

    public KafkaConsumerRunner(KafkaConsumer consumer) {
        this.consumer = consumer;
    }

    @Override
    public void run() {
        try {
            consumer.subscribe(Arrays.asList("topic-test"));
            while (!closed.get()) {
                ConsumerRecords records = consumer.poll(Duration.ofMillis(10000));
                // 执行消息处理逻辑
                System.out.println("total:::::::" + records.count());
            }
        } catch (WakeupException e) {
            // Ignore exception if closing
            if (!closed.get()) {
                throw e;
            }
        } finally {
            consumer.close();
        }
    }

    // Shutdown hook which can be called from a separate thread
    public void shutdown() {
        closed.set(true);
        consumer.wakeup();
    }
}