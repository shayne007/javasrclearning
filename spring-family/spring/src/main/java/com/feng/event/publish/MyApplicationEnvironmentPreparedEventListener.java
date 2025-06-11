package com.feng.event.publish;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author fengsy
 * @date 7/9/21
 * @Description
 */

@Slf4j
@Component
public class MyApplicationEnvironmentPreparedEventListener
    implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    @Override
    public void onApplicationEvent(final ApplicationEnvironmentPreparedEvent event) {
        log.info("{} received: {}", this.toString(), event);
    }

}
