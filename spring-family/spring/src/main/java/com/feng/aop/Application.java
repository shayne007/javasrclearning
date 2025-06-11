package com.feng.aop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.support.TaskUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author fengsy
 * @date 1/6/21
 * @Description
 */

@SpringBootApplication
@Slf4j
@EnableAspectJAutoProxy(exposeProxy = true)
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}