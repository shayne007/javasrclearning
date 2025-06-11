package com.feng.kafka.adminclient;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.DescribeLogDirsResult;
import org.apache.kafka.clients.admin.ListConsumerGroupOffsetsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.requests.DescribeLogDirsResponse;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author fengsy
 * @date 3/24/21
 * @Description
 * @see AdminClient
 */
public class KafkaAdmin {
    public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException {

        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "110.42.251.23:9092");
        props.put(AdminClientConfig.REQUEST_TIMEOUT_MS_CONFIG, 600000);

//        testTopic(props);

//        testGroup(props);

        testBroker(props);
    }

    private static void testGroup(Properties props) throws InterruptedException, ExecutionException, TimeoutException {
        String groupID = "test-group";
        try (AdminClient client = AdminClient.create(props)) {
            ListConsumerGroupOffsetsResult result = client.listConsumerGroupOffsets(groupID);
            Map<TopicPartition, OffsetAndMetadata> offsets =
                    result.partitionsToOffsetAndMetadata().get(10, TimeUnit.SECONDS);
            System.out.println(offsets);
        }
    }

    private static void testTopic(Properties props) throws InterruptedException, ExecutionException, TimeoutException {
        String newTopicName = "test-topic";
        try (AdminClient client = AdminClient.create(props)) {
            NewTopic newTopic = new NewTopic(newTopicName, 10, (short) 3);
            CreateTopicsResult result = client.createTopics(Arrays.asList(newTopic));
            result.all().get(10, TimeUnit.SECONDS);
        }
    }

    private static void testBroker(Properties props) throws ExecutionException, InterruptedException {

        try (AdminClient client = AdminClient.create(props)) {
            // 指定Broker id
            Integer targetBrokerId = 1;
            DescribeLogDirsResult ret = client.describeLogDirs(Collections.singletonList(targetBrokerId));

            long size = 0L;
            for (Map<String, DescribeLogDirsResponse.LogDirInfo> logDirInfoMap : ret.all().get().values()) {
                size += logDirInfoMap.values().stream().map(logDirInfo -> logDirInfo.replicaInfos)
                        .flatMap(topicPartitionReplicaInfoMap -> topicPartitionReplicaInfoMap.values().stream()
                                .map(replicaInfo -> replicaInfo.size))
                        .mapToLong(Long::longValue).sum();
            }
            System.out.println(size);
        }
    }
}
