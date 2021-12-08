package com.feng.concurrency.shareresource;

/**
 * @author fengsy
 * @date 5/12/21
 * @Description
 */
public class SynchronizedEvenGenerator extends IntGenerator {
    private int currentEvenValue = 0;

    @Override
    synchronized public int next() {
        ++currentEvenValue;
        Thread.yield();
        ++currentEvenValue;
        return currentEvenValue;
    }

    public static void main(String[] args) {
        EvenChecker.test(new SynchronizedEvenGenerator(), 5);
    }
}
