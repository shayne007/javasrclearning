package com.feng.amqp.compensation;

import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description 配置类 装载rabbit mq需要的spring bean
 * @Author fengsy
 * @Date 10/29/21
 */
@Configuration
public class RabbitConfiguration {

    public static final String QUEUE = "newuserQueueCompensation";
    public static final String EXCHANGE = "newuserExchangeCompensation";
    public static final String ROUTING_KEY = "newuserRoutingCompensation";

    @Bean
    public Declarables declarables() {
        Queue queue = new Queue(QUEUE);
        DirectExchange directExchange = new DirectExchange(EXCHANGE, true, false);
        return new Declarables(queue, directExchange,
                BindingBuilder.bind(queue).to(directExchange).with(ROUTING_KEY));
    }

//    @Bean
//    public Queue queue() {
//        return new Queue(QUEUE);
//    }
//
//    @Bean
//    public Exchange exchange() {
//        return ExchangeBuilder.directExchange(EXCHANGE).durable(true).build();
//    }
//
//    @Bean
//    public Binding binding() {
//        return BindingBuilder.bind(queue()).to(exchange()).with(ROUTING_KEY).noargs();
//    }

}
