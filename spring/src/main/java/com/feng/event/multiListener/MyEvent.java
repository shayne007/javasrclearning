package com.feng.event.multiListener;

import org.springframework.context.ApplicationEvent;

/**
 * @author fengsy
 * @date 7/9/21
 * @Description
 */

public class MyEvent extends ApplicationEvent {
    public MyEvent(Object source) {
        super(source);
    }
}
