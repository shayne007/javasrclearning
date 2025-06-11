package com.feng.event.publish;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * @author fengsy
 * @date 7/9/21
 * @Description
 */

@Slf4j
@Component
public class MyContextRefreshEventListener implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        log.info("{} received: {}", this.toString(), event);
    }

}