package com.feng.amqp.basics;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @Description controller 对外提供用户注册接口
 * @Author fengsy
 * @Date 10/29/21
 */
@RestController
@RequestMapping("demo")
@Slf4j
public class UserController {
    //日期格式化
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Resource
    RabbitTemplate rabbitTemplate;

    @GetMapping("send")
    public void send() {
        String msgId = UUID.randomUUID().toString().replace("-", "").substring(0, 32);
        String sendTime = sdf.format(new Date());
        Map<String, Object> map = new HashMap<>();
        map.put("msgId", msgId);
        map.put("sendTime", sendTime);
        map.put("msg", "hello");
        //模拟部分注册消息未能发送至rabbit MQ
        rabbitTemplate.convertAndSend(Consts.RABBITMQ_DEMO_EXCHANGE, Consts.RABBITMQ_DEMO_ROUTING_KEY, map);
        log.info("convertAndSend: {}", map.get("msgId"));
    }

}
