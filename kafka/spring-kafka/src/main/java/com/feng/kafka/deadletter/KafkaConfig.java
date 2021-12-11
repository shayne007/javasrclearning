package com.feng.kafka.deadletter;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.autoconfigure.kafka.ConcurrentKafkaListenerContainerFactoryConfigurer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ConsumerRecordRecoverer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.ErrorHandler;
import org.springframework.kafka.listener.SeekToCurrentErrorHandler;
import org.springframework.util.backoff.BackOff;
import org.springframework.util.backoff.FixedBackOff;

/**
 * @Description TODO
 * @Author fengsy
 * @Date 11/1/21
 */
@Configuration
@Slf4j
public class KafkaConfig {
    @Bean
    public KafkaAdmin admin(KafkaProperties properties) {
        KafkaAdmin admin = new KafkaAdmin(properties.buildAdminProperties());
        admin.setFatalIfBrokerNotAvailable(true);
        return admin;
    }

    @Bean
    public NewTopic topic() {
        return TopicBuilder.name("topic-testdlt").partitions(2).replicas(1).build();
    }

    //    @Bean
//    @Primary
    public ErrorHandler kafkaErrorHandler(KafkaTemplate<?, ?> template) {

        log.warn("kafkaErrorHandler begin to Handle");
        ConsumerRecordRecoverer recoverer = new DeadLetterPublishingRecoverer(template);
        // 设置重试间隔 5秒 次数为 3次
        BackOff backOff = new FixedBackOff(5000L, 3L);
        return new SeekToCurrentErrorHandler(recoverer, backOff);
    }

    @Bean
    public KafkaListenerContainerFactory<?> containerFactory(
            ConcurrentKafkaListenerContainerFactoryConfigurer configurer,
            ConsumerFactory<Object, Object> kafkaConsumerFactory,
            KafkaTemplate<Object, Object> template) {
        ConcurrentKafkaListenerContainerFactory<Object, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        configurer.configure(factory, kafkaConsumerFactory);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        factory.setBatchListener(true);//批量处理消息时 不支持重试
        //最大重试三次
//        factory.setErrorHandler(kafkaErrorHandler(template));
        return factory;
    }
}
