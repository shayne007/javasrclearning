package com.feng.event.multiListener;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
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
// @EnableAsync
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public SimpleApplicationEventMulticaster setErrorHandler(SimpleApplicationEventMulticaster multicaster) {
        multicaster.setErrorHandler(TaskUtils.LOG_AND_SUPPRESS_ERROR_HANDLER);
        // multicaster.setTaskExecutor(Executors.newFixedThreadPool(10));
        return multicaster;
    }
}