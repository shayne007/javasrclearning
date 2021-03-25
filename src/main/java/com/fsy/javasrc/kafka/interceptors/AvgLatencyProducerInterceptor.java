package com.fsy.javasrc.kafka.interceptors;

import java.util.Map;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import redis.clients.jedis.Jedis;

/**
 * @author fengsy
 * @date 3/22/21
 * @Description
 */

public class AvgLatencyProducerInterceptor implements ProducerInterceptor<String, String> {

    private Jedis jedis; // 省略Jedis初始化

    @Override
    public ProducerRecord<String, String> onSend(ProducerRecord<String, String> record) {
        jedis.incr("totalSentMessage");
        return record;
    }

    @Override
    public void onAcknowledgement(RecordMetadata metadata, Exception exception) {}

    @Override
    public void close() {}

    @Override
    public void configure(Map<String, ?> configs) {}
}