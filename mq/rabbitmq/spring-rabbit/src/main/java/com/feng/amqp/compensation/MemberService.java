package com.feng.amqp.compensation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @Description 会员服务，接收消息队列任务，异步发送短信息
 * @Author fengsy
 * @Date 10/29/21
 */
@Component
@Slf4j
public class MemberService {

    private Map<Long, Boolean> statusMap = new ConcurrentHashMap<>();

    @RabbitListener(queues = RabbitConfiguration.QUEUE)
    public void listen(User user) {
        log.info("receive mq user {}", user.getId());
        send(user);
    }

    /**
     * 发送短信息接口，statusMap记录用户状态，用于实现幂等
     *
     * @param user
     */
    public void send(User user) {
        if (statusMap.putIfAbsent(user.getId(), true) == null) {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {

            }
            log.info("memberService: send welcome msg to new user {}", user.getId());
        }
    }

}
