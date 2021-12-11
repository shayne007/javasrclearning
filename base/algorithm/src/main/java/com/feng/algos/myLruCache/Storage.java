package com.feng.algos.myLruCache;

/**
 * @autor fengsy
 * @date 3/19/20
 */

/**
 * KV存储抽象
 */
public interface Storage<K, V> {
    /**
     * 根据提供的key来访问数据
     *
     * @param key 数据Key
     * @return 数据值
     */
    V get(K key);
}
