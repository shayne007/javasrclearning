package com.feng.order.util;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author fengsy
 * @date 8/3/21
 * @Description
 */
public class IdUtil {
    private static final AtomicLong IDX = new AtomicLong();

    private IdUtil() {
        // no instance
    }

    public static long nextId() {
        return IDX.incrementAndGet();
    }
}
