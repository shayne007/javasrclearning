package com.feng.jdk.concurrency.patterns.poolworker;

import com.feng.jdk.concurrency.util.Debug;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author fengsy
 * @date 5/20/21
 * @Description
 */
public class ThreadPoolSize4IOIntensiveTask {
    public static void main(String[] args) {

        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
                // 核心线程池大小为1
                1,
                // 最大线程池大小为2*Ncpu
                Runtime.getRuntime().availableProcessors() * 2, 60, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(200));

        threadPool.submit(new IOIntensiveTask());

        Debug.info("max pool size: " + threadPool.getMaximumPoolSize());
        threadPool.shutdown();
    }

    // 某个I/O密集型任务
    private static class IOIntensiveTask implements Runnable {

        @Override
        public void run() {

            // 执行大量的I/O操作
            Debug.info("Running IO-intensive task...");

        }

    }
}
