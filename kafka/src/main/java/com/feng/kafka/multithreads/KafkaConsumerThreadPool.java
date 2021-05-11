package com.feng.kafka.multithreads;

import java.time.Duration;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

/**
 * @author fengsy
 * @date 3/26/21
 * @Description
 */
public class KafkaConsumerThreadPool {

    private static ExecutorService executors;
    private static int workerNum = 10;

    public static void execute(KafkaConsumer<String, String> consumer) {

        executors = new ThreadPoolExecutor(workerNum, workerNum, 0L, TimeUnit.MILLISECONDS,
            new ArrayBlockingQueue<>(1000), new ThreadPoolExecutor.CallerRunsPolicy());

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
            for (final ConsumerRecord record : records) {
                executors.submit(new Worker(record));
            }
        }
    }

}

class Worker implements Callable {
    private ConsumerRecord consumerRecord;

    public Worker(ConsumerRecord record) {
        this.consumerRecord = record;
    }

    @Override
    public Object call() throws Exception {
        return consumerRecord;
    }
}