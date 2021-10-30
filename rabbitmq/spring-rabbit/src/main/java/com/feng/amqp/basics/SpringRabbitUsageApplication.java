package com.feng.amqp.basics;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @Description spring rabbit 的基本概念与使用
 * @Author fengsy
 * @Date 10/29/21
 */
@SpringBootApplication
public class SpringRabbitUsageApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringRabbitUsageApplication.class, args);

        ApplicationContext context =
                new AnnotationConfigApplicationContext(RabbitConfiguration.class);
        AmqpTemplate template = context.getBean(AmqpTemplate.class);
        template.convertAndSend("myqueue", "foo");
        String foo = (String) template.receiveAndConvert("myqueue");
    }
}