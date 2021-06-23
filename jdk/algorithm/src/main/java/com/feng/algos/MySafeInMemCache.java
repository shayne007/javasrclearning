package com.feng.algos;

import org.apache.commons.collections.MapIterator;
import org.apache.commons.collections.map.LRUMap;

import java.util.ArrayList;

/**
 * @author fengsy
 * @date 6/12/21
 * @Description
 */

public class MySafeInMemCache<K, T> {

    private long timeToLive;
    private LRUMap cacheMap;

    /**
     * 封装缓存对象： 设置最近访问时间属性
     */
    protected class CacheObjext {
        public long lastAccessTime = System.currentTimeMillis();
        public T value;

        protected CacheObjext(T value) {
            this.value = value;
        }
    }

    /**
     * @param timeToLive
     *            expired time
     * @param timeToLiveInterval
     *            清理缓存时间间隔
     * @param maxItems
     *            缓存大小
     */
    public MySafeInMemCache(long timeToLive, final long timeToLiveInterval, int maxItems) {
        this.timeToLive = timeToLive * 1000;
        cacheMap = new LRUMap(maxItems);
        if (timeToLive > 0 && timeToLiveInterval > 0) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            Thread.sleep(timeToLiveInterval * 1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        cleanup();
                    }

                }
            });
            // 设置缓存清理进程为守护进程
            thread.setDaemon(true);
            thread.start();
        }

    }

    public void put(K key, T value) {
        synchronized (cacheMap) {
            cacheMap.put(key, new CacheObjext(value));
        }
    }

    /**
     * @param key
     *            从缓存中获取key对应的值,访问后更新lastAccessTime
     * @return
     */
    public T get(K key) {
        synchronized (cacheMap) {
            CacheObjext cacheObjext = (CacheObjext)cacheMap.get(key);
            if (cacheObjext == null) {
                return null;
            } else {
                cacheObjext.lastAccessTime = System.currentTimeMillis();
                return cacheObjext.value;
            }
        }

    }

    public void remove(K key) {
        synchronized (cacheMap) {
            cacheMap.remove(key);
        }
    }

    public int size() {
        synchronized (cacheMap) {
            return cacheMap.size();
        }
    }

    /**
     * 清理过期数据
     */
    public void cleanup() {
        long now = System.currentTimeMillis();
        ArrayList<K> deleteKeyList;

        synchronized (cacheMap) {
            MapIterator mapIterator = cacheMap.mapIterator();
            // 清理的数据不会超过缓存大小的一半？
            deleteKeyList = new ArrayList<K>((cacheMap.size() / 2) + 1);
            K key;
            CacheObjext c;

            while (mapIterator.hasNext()) {
                key = (K)mapIterator.next();
                c = (CacheObjext)mapIterator.getValue();

                if (c != null && (now > (timeToLive + c.lastAccessTime))) {
                    deleteKeyList.add(key);
                }
            }
        }

        for (K key : deleteKeyList) {
            synchronized (cacheMap) {
                cacheMap.remove(key);
            }
            //
            Thread.yield();
        }

    }

}
