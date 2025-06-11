package com.feng.kafka.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Description TODO
 * @Author fengsy
 * @Date 11/1/21
 */
@RestController
@Slf4j
public class MessageController {

    @Resource
    private KafkaTemplate<Object, Object> template;

    @GetMapping("/send/{input}")
    public void sendFoo(@PathVariable String input) {
        this.template.send("topic-kl", input);
    }

    @GetMapping("/send2/{input}")
    public void sendFoo2(@PathVariable String input) {
        this.template.send("topic-kl", input).addCallback(new ListenableFutureCallback<SendResult<Object, Object>>() {
            @Override
            public void onFailure(Throwable e) {
                log.error("send message error: ", e);
            }

            @Override
            public void onSuccess(SendResult<Object, Object> objectObjectSendResult) {
                log.info("send message successfully: {}, result: {}", input, objectObjectSendResult);
            }
        });
    }

    @KafkaListener(id = "webGroup", topics = "topic-kl", groupId = "group-test")
    public void listen(String input) {
        log.info("input value: {}", input);
    }
}
