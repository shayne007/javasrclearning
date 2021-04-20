package com.feng.javasrc.kafka.opentracing;

import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerInterceptor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

import io.opentracing.util.GlobalTracer;

/**
 * @author fengsy
 * @date 3/22/21
 * @Description
 */
public class TracingConsumerInterceptor<K, V> implements ConsumerInterceptor<K, V> {

    @Override
    public ConsumerRecords<K, V> onConsume(ConsumerRecords<K, V> records) {
        for (ConsumerRecord<K, V> record : records) {
            TracingKafkaUtils.buildAndFinishChildSpan(record, GlobalTracer.get());
        }

        return records;
    }

    @Override
    public void onCommit(Map<TopicPartition, OffsetAndMetadata> offsets) {

    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> configs) {

    }
}
