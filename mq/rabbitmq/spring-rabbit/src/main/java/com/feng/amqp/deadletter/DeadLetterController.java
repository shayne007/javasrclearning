package com.feng.amqp.deadletter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
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
    private AmqpTemplate rabbitTemplate;

    @GetMapping("sendMessage")
    public void sendMessage() {
        String msg = "msg" + atomicLong.incrementAndGet();
        log.info("send message {}", msg);
        rabbitTemplate.convertAndSend(Consts.EXCHANGE, msg);
    }
}
