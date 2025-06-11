package com.feng.event.multiListener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author fengsy
 * @date 7/9/21
 * @Description
 */
@Slf4j
@Component
@Order(2)
// @Async
public class MySecondEventListener implements ApplicationListener<MyEvent> {
    @Override
    public void onApplicationEvent(MyEvent event) {
        log.info("{} received: {}", this.toString(), event);
    }
}
