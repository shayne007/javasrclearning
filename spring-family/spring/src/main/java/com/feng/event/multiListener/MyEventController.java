package com.feng.event.multiListener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.scheduling.support.TaskUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static org.springframework.context.support.AbstractApplicationContext.APPLICATION_EVENT_MULTICASTER_BEAN_NAME;

/**
 * @author fengsy
 * @date 7/9/21
 * @Description
 */
@RestController
@Slf4j
public class MyEventController {

    @Autowired
    private AbstractApplicationContext applicationContext;

    @RequestMapping(path = "publish", method = RequestMethod.GET)
    public String notifyEvent() {
        log.info("start to publish event");
        applicationContext.publishEvent(new MyEvent(UUID.randomUUID()));
        return "ok";
    };
}