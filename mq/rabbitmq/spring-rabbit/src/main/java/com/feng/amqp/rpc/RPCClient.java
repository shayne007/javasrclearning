package com.feng.amqp.rpc;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @Description spring framework amqp
 * @Author fengsy
 * @Date 10/28/21
 */
public class RPCClient {
    @Autowired
    private RabbitTemplate template;

    @Autowired
    private DirectExchange exchange;

    int start = 0;

    @Scheduled(fixedDelay = 1000, initialDelay = 500)
    public void send() {
        System.out.println(" [x] Requesting fib(" + start + ")");
        Integer response = (Integer) template.convertSendAndReceive(exchange.getName(), "rpc", start++);
        System.out.println(" [.] Got '" + response + "'");
    }
}
