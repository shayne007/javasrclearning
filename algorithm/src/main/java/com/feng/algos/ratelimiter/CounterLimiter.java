package com.feng.algos.ratelimiter;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * @Description 限流算法演示-计数器算法，比如：以一分钟为计算区间，一分钟内访问次数超过60次则拒绝访问。
 * @Author fengsy
 * @Date 9/21/21
 */
public class CounterLimiter {
    /**
     * 计数器限流的限制次数
     */
    private long limit;
    /**
     * 计数器限流的时间区间
     */
    private long interval;
    /**
     * 访问计数的开始时间
     */
    private volatile long startTime;
    /**
     * 记录访问的次数
     */
    private volatile int counter;

    public CounterLimiter(long limit, long interval) {
        this.limit = limit;
        this.interval = interval;
    }

    public boolean isLimited() {
        boolean flag = false;
        long currentTime = System.currentTimeMillis();

        if (currentTime - startTime > interval) {
            startTime = currentTime;
            counter = 0;
        }
        counter++;
        if (counter > limit) {
            flag = true;
        }
        return flag;
    }

    public static void main(String[] args) {
        CounterLimiter counterLimiter = new CounterLimiter(2, 10);
        ThreadPoolExecutor executor =
                new ThreadPoolExecutor(10, 10, 0, TimeUnit.SECONDS, new LinkedBlockingQueue<>(10));
        for (int i = 0; i < 1000; i++) {
            LockSupport.parkNanos(5 * 1000);
            executor.execute(() -> System.out.println(counterLimiter.isLimited()));
        }
    }
}
