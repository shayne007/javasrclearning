package com.feng.amqp.compensation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @Description TODO
 * @Author fengsy
 * @Date 10/29/21
 */
@SpringBootApplication
@EnableScheduling
public class CompensationApplication {
    public static void main(String[] args) {
        SpringApplication.run(CompensationApplication.class, args);
    }
}
