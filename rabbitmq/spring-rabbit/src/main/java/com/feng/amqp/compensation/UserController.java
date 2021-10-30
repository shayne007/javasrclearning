package com.feng.amqp.compensation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

/**
 * @Description controller 对外提供用户注册接口
 * @Author fengsy
 * @Date 10/29/21
 */
@RestController
@RequestMapping("user")
@Slf4j
public class UserController {
    @Resource
    UserService userService;
    @Resource
    RabbitTemplate rabbitTemplate;

    @GetMapping("register")
    public void register() {
        IntStream.rangeClosed(1, 10).forEach(new IntConsumer() {
            @Override
            public void accept(int i) {
                User user = userService.register();
                //模拟部分注册消息未能发送至rabbit MQ
                if (ThreadLocalRandom.current().nextInt(10) % 2 == 0) {
                    rabbitTemplate.convertAndSend(RabbitConfiguration.EXCHANGE, RabbitConfiguration.ROUTING_KEY, user);
                    log.info("sent mq user {}", user.getId());
                }
            }
        });
    }

}
