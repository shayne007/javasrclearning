package com.feng.amqp.basics;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
@RabbitListener(queues = Consts.RABBITMQ_DEMO_QUEUE)
public class MQListener {

    @RabbitHandler
    public void handler(String data) {
        log.info("got message {}, processing...", data);
    }

    @RabbitHandler
    public void handler2(Map<String, Object> data) {
        log.info("got message {}, processing...", data);
    }
}
