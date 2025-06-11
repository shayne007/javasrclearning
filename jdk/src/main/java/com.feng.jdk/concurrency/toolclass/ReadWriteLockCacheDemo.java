package com.feng.jdk.concurrency.toolclass;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author fengsy
 * @date 1/15/21
 * @Description
 */

class ReadWriteLockCacheDemo<K, V> {
    final Map<K, V> m = new HashMap<>();
    final ReadWriteLock rwl = new ReentrantReadWriteLock();
    // 读锁
    final Lock r = rwl.readLock();
    // 写锁
    final Lock w = rwl.writeLock();

    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        ReadWriteLockCacheDemo<String, String> cache = new ReadWriteLockCacheDemo<>();
        for (int i = 0; i < 10; i++) {
            pool.submit(() -> {
                String v1 = cache.get("k1");
                System.out.println(v1);
            });
        }
        cache.put("k1", "v2");

    }

    // 读缓存
    V get(K key) {
        V v;
        r.lock();
        try {
            v = m.get(key);
            System.out.println(Thread.currentThread().getName() + ",进入读锁获取缓存: " + v);
        } finally {
            r.unlock();
        }

        if (v != null) {
            return v;
        }

        w.lock();
        try {
            v = m.get(key);
            System.out.println(Thread.currentThread().getName() + ",进入写锁获取缓存: " + v);
            if (v == null) {
                v = getFromDB(key);
                System.out.println(Thread.currentThread().getName() + ",进入写锁获取缓存失败，查询数据库: " + v);
                m.put(key, v);
            }
        } finally {
            w.unlock();
        }

        return v;

    }

    private V getFromDB(K key) {
        try {
            Thread.sleep(2000);
            System.out.println("getFromDB....");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return (V) "v1";
    }

    // 写缓存
    V put(K key, V value) {
        w.lock();
        try {
            System.out.println(Thread.currentThread().getName() + ",进入写锁写缓存");
            return m.put(key, value);
        } finally {
            w.unlock();
        }
    }
}