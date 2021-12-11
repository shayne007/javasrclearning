package com.feng.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

/**
 * @Description TODO
 * @Author fengsy
 * @Date 11/1/21
 */
@Configuration
public class KafkaConfig {
    @Bean
    public KafkaAdmin admin(KafkaProperties properties) {
        KafkaAdmin admin = new KafkaAdmin(properties.buildAdminProperties());
        admin.setFatalIfBrokerNotAvailable(true);
        return admin;
    }

    @Bean
    public NewTopic topic2() {
        return new NewTopic("topic-kl", 1, (short) 1);
    }

    @Bean
    public NewTopic topic() {
        return TopicBuilder.name("topic-test").partitions(2).replicas(2).build();
    }
}
