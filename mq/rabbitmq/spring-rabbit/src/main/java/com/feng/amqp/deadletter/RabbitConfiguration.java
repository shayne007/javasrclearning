package com.feng.amqp.deadletter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;

import javax.annotation.Resource;

@Configuration
@Slf4j
public class RabbitConfiguration {

    @Resource
    private AmqpTemplate rabbitTemplate;

//    @Bean
//    public Declarables declarables() {
//        Queue queue = new Queue(Consts.QUEUE);
//        DirectExchange directExchange = new DirectExchange(Consts.EXCHANGE);
//        return new Declarables(queue, directExchange,
//                BindingBuilder.bind(queue).to(directExchange).with(Consts.ROUTING_KEY));
//    }

    @Bean
    public Declarables declarablesForDead() {
        Queue queue = new Queue(Consts.DEAD_QUEUE);
        DirectExchange directExchange = new DirectExchange(Consts.DEAD_EXCHANGE);
        return new Declarables(queue, directExchange,
                BindingBuilder.bind(queue).to(directExchange).with(Consts.DEAD_ROUTING_KEY));
    }

    @Bean
    public RetryOperationsInterceptor interceptor() {
        return RetryInterceptorBuilder.stateless()
                .maxAttempts(5)
                .backOffOptions(1000, 2.0, 10000)
                .recoverer(new RepublishMessageRecoverer(rabbitTemplate, Consts.DEAD_EXCHANGE,
                        Consts.DEAD_ROUTING_KEY))
                .build();
    }

//    @Bean
//    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
//            ConnectionFactory connectionFactory) {
//        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
//        factory.setConnectionFactory(connectionFactory);
//        factory.setAdviceChain(interceptor());
//        factory.setConcurrentConsumers(10);
//        return factory;
//    }

    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
                                             MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setConcurrency("10");
        container.setAdviceChain(interceptor());
        container.setQueueNames(Consts.QUEUE, Consts.DEAD_QUEUE);
        container.setMessageListener(listenerAdapter);
        return container;
    }

    @Bean
    MessageListenerAdapter listenerAdapter(MQListener receiver) {
        MessageListenerAdapter adapter = new MessageListenerAdapter(receiver);
        adapter.addQueueOrTagToMethodName(Consts.QUEUE, "handler");
        adapter.addQueueOrTagToMethodName(Consts.DEAD_QUEUE, "deadHandler");
        return adapter;
    }
}
