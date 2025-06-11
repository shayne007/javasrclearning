package com.feng.redis.util;

import java.util.concurrent.locks.LockSupport;

/**
 * @Description TODO
 * @Author fengsy
 * @Date 11/23/21
 */
public class ThreadUtil {
    /**
     * 线程睡眠
     *
     * @param second 秒
     */
    public static void sleepSeconds(int second) {
        LockSupport.parkNanos(second * 1000L * 1000L * 1000L);
    }

    /**
     * 线程睡眠
     *
     * @param millisecond 毫秒
     */
    public static void sleepMilliSeconds(long millisecond) {
        LockSupport.parkNanos(millisecond * 1000L * 1000L);
    }

}
