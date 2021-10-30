package com.feng.amqp.basics;

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
        cf.setVirtualHost("/feng");
        cf.setConnectionThreadFactory(new CustomizableThreadFactory("my-rabbit-"));
        cf.setConnectionLimit(10);
        cf.setConnectionNameStrategy(connectionFactory -> "MY_CONNECTION");
        cf.setCacheMode(CachingConnectionFactory.CacheMode.CONNECTION);
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
//       return QueueBuilder.durable("myqueue").autoDelete().exclusive().build();
        return new Queue("myqueue");
    }


}
