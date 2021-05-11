package com.feng.kafka;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.errors.AuthorizationException;
import org.apache.kafka.common.errors.OutOfOrderSequenceException;
import org.apache.kafka.common.errors.ProducerFencedException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class KafkaProducerUtil {

    /**
     * The producer is thread safe and sharing a single producer instance across threads will generally be faster than
     * having multiple instances.
     */
    private static KafkaProducer<String, String> producer;

    /**
     * 初始化producer实例
     */
    private static void initProducerParams(String brokerList, String groupId) {
        Properties props = new Properties();
        props.put("bootstrap.servers", brokerList);
        props.put("group.id", groupId);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("acks", "all");
        props.put("retries", 3);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("partitioner.class", "com.fsy.javasrc.kafka.partitioner.DefaultPartitioner");
        // 开启GZIP压缩
        props.put("compression.type", "gzip");
        // 开启幂等producer
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);

        List<String> interceptors = new ArrayList<>();
        interceptors.add("com.fsy.javasrc.kafka.interceptors.AddTimeStampInterceptor");
        interceptors.add("com.fsy.javasrc.kafka.interceptors.UpdateCounterInterceptor");
        props.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG, interceptors);

        producer = new KafkaProducer<>(props);
    }

    /**
     * 单例方式获取producer实例
     */
    private static KafkaProducer<String, String> getProducer(String brokerList, String groupId) {
        if (null == producer) {
            initProducerParams(brokerList, groupId);
        }
        return producer;
    }

    /**
     * 推送消息
     */
    public static boolean sendMessage(String topic, String groupId, String value, String brokerList) {
        boolean result = true;
        KafkaProducer<String, String> producer = getProducer(brokerList, groupId);
        try {
            producer.send(new ProducerRecord<>(topic, value), (metadata, e) -> {
                log.info("coming into callback");
                if (e != null) {
                    log.error("kafka exception: {}", e.getMessage());
                    return;
                }
                log.debug("topic: {}", metadata.topic());
                log.debug("partition: {}", metadata.partition());
                log.debug("offset: {}", metadata.offset());
            });
            producer.flush();
        } catch (Exception e) {
            result = false;
            log.error("======sendMessage Exception:", e);
        }
        return result;
    }

    /**
     * 生产者发送带key的消息，用于分区按key保序策略
     *
     * @param topic
     * @param groupId
     * @param key
     * @param value
     * @param brokerList
     * @return
     */
    public static boolean sendMessageWithKey(String topic, String groupId, String key, String value,
        String brokerList) {
        boolean result = true;
        KafkaProducer<String, String> producer = getProducer(brokerList, groupId);
        try {
            producer.send(new ProducerRecord<>(topic, key, value));
            producer.flush();
        } catch (Exception e) {
            result = false;
            log.error("======sendMessage Exception:", e);
        }
        return result;
    }

    /**
     * 在事务中发送数据
     */
    public static void sendMsgTransactional(String topic, String groupId, String[] values, String brokerList) {
        KafkaProducer<String, String> producer = getProducer(brokerList, groupId);
        producer.initTransactions();
        try {
            producer.beginTransaction();
            for (String value : values) {
                producer.send(new ProducerRecord<>(topic, value));
            }
            producer.commitTransaction();
        } catch (ProducerFencedException | OutOfOrderSequenceException | AuthorizationException e) {
            // We can't recover from these exceptions, so our only option is to close the producer and exit.
            producer.close();
        } catch (KafkaException e) {
            // For all other exceptions, just abort the transaction and try again.
            producer.abortTransaction();
        }
        producer.close();
    }

}
