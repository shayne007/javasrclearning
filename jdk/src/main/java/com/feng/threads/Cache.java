package com.feng.threads;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author fengsy
 * @date 1/15/21
 * @Description
 */

class Cache<K, V> {
    final Map<K, V> m = new HashMap<>();
    final ReadWriteLock rwl = new ReentrantReadWriteLock();
    // 读锁
    final Lock r = rwl.readLock();
    // 写锁
    final Lock w = rwl.writeLock();

    // 读缓存
    V get(K key) {
        V v = null;
        r.lock();
        try {
            v = m.get(key);
        } finally {
            r.unlock();
        }

        if (v != null) {
            return v;
        }

        w.lock();
        try {
            v = m.get(key);
            if (v == null) {
                v = getFromDB(key);
                m.put(key, v);
            }
        } finally {
            w.unlock();
        }

        return v;

    }

    private V getFromDB(K key) {
        return null;
    }

    // 写缓存
    V put(K key, V value) {
        w.lock();
        try {
            return m.put(key, value);
        } finally {
            w.unlock();
        }
    }
}