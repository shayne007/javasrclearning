package com.feng.javasrc.annotation.cache;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * 缓存
 * 
 * @author fengsy
 * @date 3/10/21
 * @Description
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Cache {
    /**
     * expired time
     */
    long timeOut() default 0;

    /**
     * time unit
     * 
     * @return
     */
    TimeUnit timeUnit() default TimeUnit.HOURS;

    String cacheName();

    String key();
}
