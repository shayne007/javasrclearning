package com.feng.http;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HTTPKeepAliveTester {
    private static final Logger LOGGER = LoggerFactory.getLogger(HTTPKeepAliveTester.class);

    private String url = "http://110.42.251.23/ratelimit/demo";
    private ExecutorService pool =
        new ThreadPoolExecutor(8, 8, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(500));

    /**
     * 测试用例：使用JDK的 java.net.HttpURLConnection发起HTTP请求
     */
    @Test
    public void simpleGet() throws IOException, InterruptedException {
        /**
         * 提交的请求次数
         */
        int index = 1000000;
        while (--index > 0) {
            String target = url;
            int finalIndex = index;
            // 使用固定20个线程的线程池发起请求
            Thread.sleep(100);
            pool.submit(() -> {
                // 使用JDK的 java.net.HttpURLConnection发起HTTP请求
                String out = HttpJdkTest.jdkGet(target);
                LOGGER.info(finalIndex + ": out = " + out);
            });
        }
        Thread.sleep(Integer.MAX_VALUE);
    }

    /**
     * 测试用例：使用带连接池的Apache HttpClient提交的HTTP请求
     */
    @Test
    public void pooledGet() throws IOException, InterruptedException {
        int index = 1000000;
        while (--index > 0) {
            String target = url;
            // 使用固定20个线程的线程池发起请求
            int finalIndex = index;
            Thread.sleep(100);
            pool.submit(() -> {
                // 使用Apache HttpClient提交的HTTP请求
                String out = PooledHttpClientUtil.get(target);
                LOGGER.info(finalIndex + ": out = " + out);
            });
        }
        Thread.sleep(Integer.MAX_VALUE);
    }

    @Test
    public void continuePooledGet() throws IOException, InterruptedException {
        int index = 1000000;
        while (--index > 0) {
            String target = url;
            int finalIndex = index;
            String out = PooledHttpClientUtil.get(target);
            LOGGER.info(finalIndex + "out = " + out);
            // 睡眠1s
            Thread.sleep(1000);
        }

    }
}
