package com.feng.kafka;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.errors.AuthorizationException;
import org.apache.kafka.common.errors.OutOfOrderSequenceException;
import org.apache.kafka.common.errors.ProducerFencedException;
import org.apache.kafka.common.serialization.StringSerializer;

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
    private static void initProducerParams(String brokerList) {
        Properties props = new Properties();
        try {
            props.put(ConsumerConfig.CLIENT_ID_CONFIG, InetAddress.getLocalHost().getHostName());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerList);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        // 所有副本broker都接收到消息，该消息才算"已提交"
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        // 重试次数，当出现网络的瞬时抖动时，消息发送可能会失败，此时配置了 retries > 0 的 Producer 能够自动重试消息发送
        props.put(ProducerConfig.RETRIES_CONFIG, 3);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        // 分区策略
        props.put("partitioner.class", "com.feng.kafka.partitioner.DefaultPartitioner");
        // 开启GZIP压缩
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "gzip");
        // 开启幂等producer
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);

        List<String> interceptors = new ArrayList<>();
        interceptors.add("com.feng.kafka.interceptors.producer.AddTimeStampInterceptor");
        interceptors.add("com.feng.kafka.interceptors.producer.UpdateCounterInterceptor");
        props.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG, interceptors);

        producer = new KafkaProducer<>(props);
    }

    /**
     * 单例方式获取producer实例
     */
    private static KafkaProducer<String, String> getProducer(String brokerList) {
        if (null == producer) {
            initProducerParams(brokerList);
        }
        return producer;
    }

    /**
     * 推送消息,为了防止数据丢失，使用异步推送消息的方法时，需要添加callback回调，如果发生异常可以进行相应的处理；
     * 
     */
    public static boolean sendMessage(String topic, String value, String brokerList) {
        boolean result = true;
        KafkaProducer<String, String> producer = getProducer(brokerList);
        try {
            final ProducerRecord record = new ProducerRecord<>(topic, value);
            producer.send(record, (metadata, e) -> {
                log.info("coming into callback");
                if (e != null) {
                    log.error("kafka exception: {}", e.getMessage());
                    e.printStackTrace();
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
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 生产者发送带key的消息，用于分区按key保序策略
     *
     * @param topic
     * @param key
     * @param value
     * @param brokerList
     * @return
     */
    public static boolean sendMessageWithKey(String topic, String key, String value, String brokerList) {
        boolean result = true;
        KafkaProducer<String, String> producer = getProducer(brokerList);
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
    public static void sendMsgTransactional(String topic, String[] values, String brokerList) {
        KafkaProducer<String, String> producer = getProducer(brokerList);
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
