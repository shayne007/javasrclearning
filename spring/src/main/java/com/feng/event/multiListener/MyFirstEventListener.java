package com.feng.event.multiListener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * @author fengsy
 * @date 7/9/21
 * @Description
 */
@Slf4j
@Component
@Order(1)
// @Async
public class MyFirstEventListener implements ApplicationListener<MyEvent> {
    Random random = new Random();

    @Override
    public void onApplicationEvent(MyEvent event) {
        log.info("{} received: {}", this.toString(), event);
        // 模拟部分失效
        if (random.nextInt(10) % 2 == 1)
            throw new RuntimeException("exception happen on first listener");
    }
}