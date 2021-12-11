package com.feng.kafka.async;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @Description TODO
 * @Author fengsy
 * @Date 11/1/21
 */
@SpringBootApplication
@EnableAsync
public class SpringKafkaDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringKafkaDemoApplication.class, args);
    }
}
