package com.feng.event.publish;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.scheduling.annotation.EnableAsync;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.support.TaskUtils;

/**
 * @author fengsy
 * @date 1/6/21
 * @Description
 */

@SpringBootApplication
@Slf4j
@EnableAsync
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

        // MyApplicationEnvironmentPreparedEventListener listener = new MyApplicationEnvironmentPreparedEventListener();
        // SpringApplication springApplication =
        // new SpringApplicationBuilder(Application.class).listeners(listener).build();
        // springApplication.run(args);

    }
}