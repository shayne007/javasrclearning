package com.feng.kafka.multithreads;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.apache.kafka.clients.consumer.CommitFailedException;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.consumer.OffsetCommitCallback;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;

import lombok.extern.slf4j.Slf4j;

/**
 * @author fengsy
 * @date 3/23/21
 * @Description
 */
@Slf4j
public abstract class KafkaConsumerRunner<K, V> implements Runnable {
    private final KafkaConsumer<K, V> consumer;
    private final List<String> topics;
    private final Long timeout;

    private final CountDownLatch shutdownLatch;

    public KafkaConsumerRunner(KafkaConsumer<K, V> consumer, List<String> topics, Long timeout) {
        this.consumer = consumer;
        this.topics = topics;
        this.timeout = timeout;
        this.shutdownLatch = new CountDownLatch(1);
    }

    @Override
    public void run() {
        long setTimeout = 10000;
        if (timeout != null) {
            setTimeout = timeout;
        }
        try {
            consumer.subscribe(topics);
            while (true) {
                ConsumerRecords<K, V> records = consumer.poll(Duration.ofMillis(setTimeout));
                // 执行消息处理逻辑
                handleFetchedRecords(records);
                consumer.commitAsync(new OffsetCommitCallback() {
                    @Override
                    public void onComplete(Map<TopicPartition, OffsetAndMetadata> offsets, Exception e) {
                        if (e != null) {
                            log.debug("Commit failed for offsets {}", offsets, e);
                        }
                    }
                });
                System.out.println("total:::::::" + records.count());

            }

        } catch (CommitFailedException e) {
            log.debug("Commit failed", e);

        } catch (WakeupException e) {
            // ignore

        } catch (Exception e) {
            log.error("Unexpected error", e);
        } finally {
            try {
                consumer.commitSync(); // 最后一次提交使用同步阻塞式提交
            } finally {
                consumer.close();
            }
            shutdownLatch.countDown();

        }
    }

    protected abstract void handleFetchedRecords(ConsumerRecords<K, V> records);

    public void shutdown() throws InterruptedException {
        consumer.wakeup();
        shutdownLatch.await();
    }
}