package com.feng.amqp.rabbitmqdlx;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.atomic.AtomicLong;

@RequestMapping("deadletter")
@Slf4j
@RestController
public class DeadLetterController {

    AtomicLong atomicLong = new AtomicLong();

    @Resource
    private RabbitTemplate rabbitTemplate;

    @GetMapping("send")
    public void send() {
        String message = "msg" + atomicLong.incrementAndGet();
        log.info("Client 发送消息至MQ {}", message);
        rabbitTemplate.convertAndSend(Consts.EXCHANGE, Consts.QUEUE, message);
    }
}
