package com.feng.concurrency.threading;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Description ThreadPool正确创建可复用的线程池
 * @Author fengsy
 * @Date 10/27/21
 */
public class ThreadPoolHelper {
    private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
            10, 50,
            2, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(1000),
            new ThreadFactoryBuilder().setNameFormat("demo-threadpool-%d").build(),
            new ThreadPoolExecutor.AbortPolicy());

    public static ThreadPoolExecutor getRightThreadPool() {
        return threadPoolExecutor;
    }
}