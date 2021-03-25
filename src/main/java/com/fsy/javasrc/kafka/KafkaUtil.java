package com.fsy.javasrc.kafka;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import org.apache.kafka.clients.consumer.CommitFailedException;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.consumer.StickyAssignor;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class KafkaUtil {

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

        List<String> interceptors = new ArrayList<>();
        interceptors.add("com.fsy.javasrc.kafka.interceptors.AddTimeStampInterceptor");
        interceptors.add("com.fsy.javasrc.kafka.interceptors.UpdateCounterInterceptor");
        props.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG, interceptors);

        producer = new KafkaProducer<>(props);
    }

    /**
     * 初始化consumer实例
     */
    public static KafkaConsumer<String, String> initConsumerParams(String brokerList, String topic, String groupId,
        Integer partition) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerList);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG, StickyAssignor.class.getName());

        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "2000");

        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        // 1.创建消费者
        try (final KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)) {
            consumer.subscribe(Collections.singleton(topic));
            consumer.poll(0);
            consumer.seekToBeginning(consumer.partitionsFor(topic).stream()
                .map(partitionInfo -> new TopicPartition(topic, partitionInfo.partition()))
                .collect(Collectors.toList()));
            // 消费topic主题下指定分区partition的消息
            if (partition != null) {
                TopicPartition p = new TopicPartition(topic, partition);
                consumer.assign(Arrays.asList(p));
            }
            return consumer;
        }

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
     * 接收消息
     */
    public static ConsumerRecords<String, String> receiveMessage(String brokerList, String topic, String groupId,
        Long timeout, Integer partition) {

        long setTimeout = 100;
        if (timeout != null) {
            setTimeout = timeout;
        }

        KafkaConsumer<String, String> kafkaConsumer = initConsumerParams(brokerList, topic, groupId, partition);
        ConsumerRecords<String, String> poll = kafkaConsumer.poll(Duration.ofSeconds(setTimeout));
        return poll;
    }

    /**
     * 每处理 100 条消息就提交一次位移
     * 
     * @param brokerList
     * @param topic
     * @param groupId
     * @param timeout
     * @param partition
     */
    public static void handleData(String brokerList, String topic, String groupId, Long timeout, Integer partition) {
        KafkaConsumer<String, String> consumer = initConsumerParams(brokerList, topic, groupId, partition);

        Map<TopicPartition, OffsetAndMetadata> offsets = new HashMap<>();
        int count = 0;
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
            for (ConsumerRecord<String, String> record : records) {
                // 处理数据
                // handle()
                offsets.put(new TopicPartition(record.topic(), record.partition()),
                    new OffsetAndMetadata(record.offset() + 1));
                if (count % 100 == 0) {
                    // 回调处理逻辑是null
                    consumer.commitAsync(offsets, null);
                }
                count++;
            }
        }
    }

    /**
     * 对于常规性、阶段性的手动提交，我们调用 commitAsync() 避免程序阻塞，而在 Consumer 要关闭前，我们调用 commitSync() 方法执行同步阻塞式的位移提交，以确保 Consumer
     * 关闭前能够保存正确的位移数据。将两者结合后，我们既实现了异步无阻塞式的位移管理，也确保了 Consumer 位移的正确性
     * 
     * @param brokerList
     * @param topic
     * @param groupId
     * @param timeout
     * @param partition
     */
    public static void handleData2(String brokerList, String topic, String groupId, Long timeout, Integer partition) {

        long setTimeout = 100;
        if (timeout != null) {
            setTimeout = timeout;
        }

        KafkaConsumer<String, String> consumer = initConsumerParams(brokerList, topic, groupId, partition);

        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(setTimeout));
                for (ConsumerRecord<String, String> record : records) {
                    System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(),
                        record.value());
                }
                consumer.commitAsync();
            }
        } catch (CommitFailedException e) {
            // 处理提交失败异常
        } finally {
            try {
                consumer.commitSync(); // 最后一次提交使用同步阻塞式提交
            } finally {
                consumer.close();
            }

        }

    }

}
