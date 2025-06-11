package com.feng.amqp.deadletter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MQListener {
    //    @RabbitListener(queues = Consts.QUEUE)
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = Consts.QUEUE, durable = "true"),
            exchange = @Exchange(value = Consts.EXCHANGE, ignoreDeclarationExceptions = "true"),
            key = Consts.ROUTING_KEY)
    )
    public void handler(String data) {
        log.info("got message {}, processing...", data);
//        throw new NullPointerException("error");
        throw new AmqpRejectAndDontRequeueException("error");
    }

    //    @RabbitListener(queues = Consts.DEAD_QUEUE)
    public void deadHandler(String data) {
        log.error("got dead message {}", data);
    }
}
