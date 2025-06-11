package com.feng.kafka.multithreads;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.List;

/**
 * @author fengsy
 * @date 7/31/21
 * @Description
 */
public class SingleThreadKafkaConsumerRunner extends KafkaConsumerRunner<String, String> {

    public SingleThreadKafkaConsumerRunner(KafkaConsumer<String, String> consumer, List<String> topics, Long timeout) {
        super(consumer, topics, timeout);
    }

    @Override
    protected void handleFetchedRecords(ConsumerRecords<String, String> records) {
        records.forEach(record -> process(record));
    }

    private void process(ConsumerRecord<String, String> record) {
        System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
    }
}
