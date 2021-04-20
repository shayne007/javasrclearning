package com.feng.javasrc.collections;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author fengsy
 * @date 1/21/21
 * @Description
 */
public class LinkedHashMapLRUCache<K, V> extends LinkedHashMap<K, V> {
    private static final int MAXSIZE = 100;

    private int limit;

    LinkedHashMapLRUCache() {
        this(MAXSIZE);
    }

    LinkedHashMapLRUCache(int limit) {
        super(limit, 0.75f, true);
        this.limit = limit;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry eldest) {
        return size() > limit;
    }

    public static void main(String[] args) {
        LinkedHashMapLRUCache<Integer, Integer> cache = new LinkedHashMapLRUCache<>(3);
        for (int i = 0; i < 10; i++) {
            cache.put(i, i * i);
        }
        System.out.println(cache);
        cache.get(7);
        cache.get(1);
        System.out.println(cache);
        cache.put(1, 1);
        System.out.println(cache);
    }
}
