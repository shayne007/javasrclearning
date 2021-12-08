package com.feng.kafka.multithreads;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

/**
 * @author fengsy
 * @date 7/31/21
 * @Description
 */
public class ThreadPoolKafkaConsumerRunner extends KafkaConsumerRunner<String, String> {
    private ExecutorService executor =
        new ThreadPoolExecutor(8, 8, 100, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(8));

    public ThreadPoolKafkaConsumerRunner(KafkaConsumer<String, String> consumer, List<String> topics, Long timeout) {
        super(consumer, topics, timeout);
    }

    @Override
    protected void handleFetchedRecords(ConsumerRecords<String, String> records) {
        records.partitions().forEach(partition -> {
            List<ConsumerRecord<String, String>> partitionRecords = records.records(partition);
            Task task = new Task(partitionRecords);
            executor.submit(task);
        });
    }

    public class Task implements Runnable {

        private final List<ConsumerRecord> records;

        public Task(List records) {
            this.records = records;
        }

        @Override
        public void run() {
            for (ConsumerRecord record : records) {
                System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
            }
        }
    }
}
