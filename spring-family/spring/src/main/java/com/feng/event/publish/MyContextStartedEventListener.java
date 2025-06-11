package com.feng.event.publish;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.stereotype.Component;

/**
 * @author fengsy
 * @date 7/9/21
 * @Description
 */

@Slf4j
@Component
public class MyContextStartedEventListener implements ApplicationListener<ContextStartedEvent> {

    @Override
    public void onApplicationEvent(final ContextStartedEvent event) {
        log.info("{} received: {}", this.toString(), event);
    }

}