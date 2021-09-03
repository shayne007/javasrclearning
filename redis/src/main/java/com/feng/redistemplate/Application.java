package com.feng.redistemplate;

import java.util.Collections;

import org.springframework.beans.BeansException;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author fengsy
 * @date 7/28/21
 * @Description
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
