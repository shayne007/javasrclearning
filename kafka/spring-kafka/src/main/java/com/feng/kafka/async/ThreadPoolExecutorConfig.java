package com.feng.kafka.config;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Description TODO
 * @Author fengsy
 * @Date 11/12/21
 */
@Configuration
public class ThreadPoolExecutorConfig {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * 获得Java虚拟机可用的处理器个数 + 1
     */
    private static final int THREADS = Runtime.getRuntime().availableProcessors() + 1;

    private int corePoolSize = THREADS;

    private int maxPoolSize = 2 * THREADS;

    private int queueCapacity = 1024;

    private String namePrefix = "async-service-";

    final ThreadFactory threadFactory = new ThreadFactoryBuilder()
            // -%d不要少
            .setNameFormat(namePrefix + "%d").setDaemon(true).build();

    /**
     * @return
     */
    @Bean("asyncExecutor")
    public Executor asyncExecutor() {
        return new ThreadPoolExecutor(corePoolSize, maxPoolSize, 5, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(queueCapacity), threadFactory, (r, executor) -> {
            // 打印日志,添加监控等
            logger.info("task is rejected!");
        });
    }
}