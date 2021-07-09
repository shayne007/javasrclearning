package com.feng.event.publish;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author fengsy
 * @date 7/9/21
 * @Description
 */
@RestController
public class PublishEventController {

    @Autowired
    private AbstractApplicationContext applicationContext;

    @RequestMapping(path = "publishEvent", method = RequestMethod.GET)
    public String notifyEvent() {
        applicationContext.start();
        return "ok";
    };
}