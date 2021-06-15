package com.feng.myLruCache;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 基于支持访问顺序的LinkedHashMap实现
 * 
 * @autor fengsy
 * @date 3/19/20
 *
 */

public class MyLruCache<K, V> extends LruCache<K, V> {
    private LinkedHashMap<K, V> cache;
    private Lock keyLock = new ReentrantLock(false);

    public MyLruCache(int capacity, Storage lowSpeedStorage) {
        super(capacity, lowSpeedStorage);
        this.cache = new InnerLinkedHashMap<>(capacity, 0.75f, true);
    }

    public Set<K> getKeys() {
        return cache.keySet();
    }

    @Override
    public V get(K key) {
        return getVal(key);

    }

    private V getVal(K key) {
        V val;
        keyLock.lock();
        try {
            val = cache.get(key);
        } finally {
            keyLock.unlock();
        }

        if (val == null) {
            V v = lowSpeedStorage.get(key);
            if (v == null) {
                return null;
            }

            keyLock.lock();
            try {
                cache.put(key, v);
            } finally {
                keyLock.unlock();
            }
            return v;
        }
        return val;
    }

    final class InnerLinkedHashMap<A, B> extends LinkedHashMap<A, B> {

        InnerLinkedHashMap(int initialCapacity, float loadFactor, boolean accessOrder) {
            super(initialCapacity, loadFactor, accessOrder);
        }

        @Override
        protected boolean removeEldestEntry(Map.Entry<A, B> eldest) {
            return this.size() > capacity;
        }
    }

}
