package com.feng.concurrency.shareresource;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author fengsy
 * @date 5/12/21
 * @Description
 */
public class MutexEvenGenerator extends IntGenerator {
    private int currentIntValue = 0;
    private ReentrantLock lock = new ReentrantLock();

    @Override
    public int next() {
        lock.lock();
        try {
            ++currentIntValue;
            Thread.yield();
            ++currentIntValue;
            return currentIntValue;
        } finally {
            lock.unlock();
        }

    }
}
