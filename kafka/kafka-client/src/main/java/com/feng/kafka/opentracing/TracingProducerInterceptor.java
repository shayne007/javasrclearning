package com.feng.kafka.opentracing;

import io.opentracing.util.GlobalTracer;
import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Map;

/**
 * @author fengsy
 * @date 3/22/21
 * @Description
 */
public class TracingProducerInterceptor<K, V> implements ProducerInterceptor<K, V> {

    @Override
    public ProducerRecord<K, V> onSend(ProducerRecord<K, V> producerRecord) {
        TracingKafkaUtils.buildAndInjectSpan(producerRecord, GlobalTracer.get()).finish();
        return producerRecord;
    }

    @Override
    public void onAcknowledgement(RecordMetadata recordMetadata, Exception e) {
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}
