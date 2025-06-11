package com.feng.springweb.exception;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * @author fengsy
 * @date 1/6/21
 * @Description
 */
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@ServletComponentScan
public class SpringWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringWebApplication.class, args);
    }
}