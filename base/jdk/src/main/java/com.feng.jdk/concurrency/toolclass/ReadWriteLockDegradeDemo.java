package com.feng.jdk.concurrency.toolclass;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * ReadWriteLock写锁降级为读锁
 *
 * @author fengsy
 * @date 1/15/21
 * @Description
 */

class ReadWriteLockDegradeDemo {
    Object data;
    volatile boolean cacheValid;
    final ReadWriteLock rwl = new ReentrantReadWriteLock();
    // 读锁
    final Lock r = rwl.readLock();
    // 写锁
    final Lock w = rwl.writeLock();

    public static void main(String[] args) {
        ReadWriteLockDegradeDemo lockDegrade = new ReadWriteLockDegradeDemo();
        ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        for (int i = 0; i < 10; i++) {
            pool.submit(() -> {
                lockDegrade.processCachedData();
            });
        }
    }

    void processCachedData() {
        // 获取读锁
        r.lock();
        System.out.println(Thread.currentThread().getName() + ",进入读锁读取数据");
        if (!cacheValid) {
            // 释放读锁，因为不允许读锁的升级
            r.unlock();
            // 获取写锁
            w.lock();
            System.out.println(Thread.currentThread().getName() + ",进入写锁读取数据");
            try {
                // 再次检查状态
                if (!cacheValid) {
                    data = getData();
                    cacheValid = true;
                }
                // 释放写锁前，降级为读锁
                System.out.println(Thread.currentThread().getName() + ",释放写锁前，降级为读锁");
                // 降级是可以的
                r.lock();
            } finally {
                // 释放写锁
                w.unlock();
            }
        }
        // 此处仍然持有读锁
        try {
            use(data);
        } finally {
            r.unlock();
        }
    }

    private void use(Object data) {
        System.out.println("do something with data: " + data);
    }

    private Object getData() {
        try {
            Thread.sleep(2000);
            System.out.println("getData....");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new Object();
    }
}