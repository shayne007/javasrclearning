package com.feng.redis.cache;

import org.springframework.beans.BeansException;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Collections;

/**
 * @author fengsy
 * @date 7/28/21
 * @DescriptionJedis jedis = getJedis();
 */
@SpringBootApplication
public class Application implements ApplicationRunner, ApplicationContextAware {
    private ApplicationContext applicationContext;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        CacheService cacheService = applicationContext.getBean(CacheService.class);
        cacheService.check();
        cacheService.saveCache(Collections.singletonList(new CacheService.Person(11L, "shayne")));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
