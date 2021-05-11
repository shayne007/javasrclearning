package com.feng.kafka1.dd;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.consumer.CommitFailedException;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.consumer.StickyAssignor;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import com.feng.kafka1.dd.callback.LoggingOffsetCommitCallback;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class KafkaConsumerUtil {

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
        final KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        // 指定消费主题
        // Note that it isn't possible to mix manual partition assignment (i.e. using assign) with dynamic partition
        // assignment through topic subscription (i.e. using subscribe).
        consumer.subscribe(Collections.singleton(topic));

        // // 消费topic主题下指定分区partition的消息
        if (partition != null) {
            TopicPartition p = new TopicPartition(topic, partition);
            consumer.assign(Arrays.asList(p));
        }
        return consumer;
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
     * 对于常规性、阶段性的手动提交，我们调用 commitAsync() 避免程序阻塞，而在 Consumer 要关闭前，我们调用 commitSync() 方法执行同步阻塞式的位移提交，以确保 Consumer
     * 关闭前能够保存正确的位移数据。将两者结合后，我们既实现了异步无阻塞式的位移管理，也确保了 Consumer 位移的正确性
     * 
     * @param brokerList
     * @param topic
     * @param groupId
     * @param timeout
     * @param partition
     */
    public static void handleDataManualCommitOffsetBetter(String brokerList, String topic, String groupId, Long timeout,
        Integer partition) {

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

    /**
     * 手动提交位移，每处理 100 条消息就提交一次,不用再受 poll 方法返回的消息总数的限制，避免大批量的消息重新消费。
     *
     * @param brokerList
     * @param topic
     * @param groupId
     * @param timeout
     * @param partition
     */
    public static void handleDataManualCommitOffset(String brokerList, String topic, String groupId, Long timeout,
        Integer partition) {
        KafkaConsumer<String, String> consumer = initConsumerParams(brokerList, topic, groupId, partition);

        Map<TopicPartition, OffsetAndMetadata> offsets = new HashMap<>();
        int count = 0;
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(10));
            for (ConsumerRecord<String, String> record : records) {
                // 处理数据
                // handle()

                // 消费者所消费分区，下一条消息的位移record.offset() + 1
                offsets.put(new TopicPartition(record.topic(), record.partition()),
                    new OffsetAndMetadata(record.offset() + 1));
                if (count % 100 == 0) {
                    // 添加回调处理逻辑
                    consumer.commitAsync(offsets, new LoggingOffsetCommitCallback());
                }
                count++;
            }
        }
    }

    /**
     * consume a batch of records and batch them up in memory,manually commit the offsets only after the corresponding
     * records have been inserted into the database
     * 
     * @param brokerList
     * @param topic
     * @param groupId
     * @param timeout
     * @param partition
     */
    public static void batchConsumeData(String brokerList, String topic, String groupId, Long timeout,
        Integer partition) {
        KafkaConsumer<String, String> consumer = initConsumerParams(brokerList, topic, groupId, partition);
        final int minBatchSize = 200;
        List<ConsumerRecord<String, String>> buffer = new ArrayList<>();
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, String> record : records) {
                buffer.add(record);
            }
            if (buffer.size() >= minBatchSize) {
                // insertIntoDb(buffer);
                consumer.commitSync();
                buffer.clear();
            }
        }
    }

}
