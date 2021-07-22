package com.feng.springdata;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author fengsy
 * @date 1/6/21
 * @Description
 */

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class SpringdataApplication {

    SpringdataApplication(RedisTemplate redisTemplate, StringRedisTemplate stringRedisTemplate) {
        String key = "mykey";
        stringRedisTemplate.opsForValue().set(key, "myvalue");

        Object valueGotFromStringRedisTemplate = stringRedisTemplate.opsForValue().get(key);
        System.out.println(valueGotFromStringRedisTemplate);

        Object valueGotFromRedisTemplate = redisTemplate.opsForValue().get(key);
        System.out.println(valueGotFromRedisTemplate);
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringdataApplication.class, args);
    }

}