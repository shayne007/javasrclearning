package com.feng.jdk.concurrency.shareresource;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author fengsy
 * @date 5/12/21
 * @Description
 */
public class AttemptLocking {
    private ReentrantLock lock = new ReentrantLock();

    public void untimed() {
        boolean captured = lock.tryLock();
        try {
            System.out.println("tryLock(): " + captured);
        } finally {
            if (captured) {
                lock.unlock();
            }
        }
    }

    public void timed() {
        boolean captured = false;
        try {
            captured = lock.tryLock(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        try {
            System.out.println("tryLock(2,TimeUnit.SECOND): " + captured);
        } finally {
            if (captured) {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) {
        final AttemptLocking locking = new AttemptLocking();
        locking.untimed();
        locking.timed();
        new Thread() {
            {
                setDaemon(true);
            }

            @Override
            public void run() {
                locking.lock.lock();
                System.out.println("daemon thread acquired the lock.");
            }
        }.start();
        Thread.yield();
        locking.untimed();
        locking.timed();
    }
}
