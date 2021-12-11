package com.feng.amqp.basics;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

/**
 * @Description 配置类 装载rabbit mq需要的spring bean
 * @Author fengsy
 * @Date 10/29/21
 */
@Configuration
public class RabbitConfiguration {

    @Bean

    public CachingConnectionFactory connectionFactory() {
        CachingConnectionFactory cf = new CachingConnectionFactory();
        cf.setUsername("root");
        cf.setPassword("root");
        cf.setChannelCacheSize(25);
        cf.setAddresses("106.15.66.38:5672");
        cf.setShuffleAddresses(true);
        cf.setConnectionThreadFactory(new CustomizableThreadFactory("my-rabbit-"));
        cf.setConnectionLimit(10);
        cf.setConnectionNameStrategy(connectionFactory -> "MY_CONNECTION");
        return cf;
    }


    @Bean
    public RabbitAdmin amqpAdmin() {
        return new RabbitAdmin(connectionFactory());
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        return new RabbitTemplate(connectionFactory());
    }

    @Bean
    public Queue myQueue() {
        /**
         * 1、name:    队列名称
         * 2、durable: 是否持久化
         * 3、exclusive: 是否独享、排外的。如果设置为true，定义为排他队列。则只有创建者可以使用此队列。也就是private私有的。
         * 4、autoDelete: 是否自动删除。也就是临时队列。当最后一个消费者断开连接后，会自动删除。
         * */
        return new Queue(Consts.RABBITMQ_DEMO_QUEUE, true, false, false);
    }

    @Bean
    public Exchange exchange() {
        //Direct交换机
        return ExchangeBuilder.directExchange(Consts.RABBITMQ_DEMO_EXCHANGE).durable(true).build();
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(myQueue())
                .to(exchange())
                .with(Consts.RABBITMQ_DEMO_ROUTING_KEY).noargs();
    }

}
