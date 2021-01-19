package com.fsy.javasrc.threads;

/**
 * @author fengsy
 * @date 1/11/21
 * @Description
 */

class SafeCalc {
    static long value = 0L;

    synchronized long get() {
        return value;
    }

    synchronized static void addOne() {
        value += 1;
    }
}
