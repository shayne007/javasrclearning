package com.feng.dataStructures.array;

import java.util.HashMap;
import java.util.Map;

/**
 * @author fengsy
 * @date 6/15/21
 * @Description
 */
public class LRUBasedArray<T> {
    private static final int DEFAULT_CAPACITY = 1 << 3;
    private int capacity;
    private int count;
    private T[] value;
    private Map<T, Integer> holder;// 存储数组中每个元素的位置，空间换时间

    public LRUBasedArray() {
        this(DEFAULT_CAPACITY);
    }

    public LRUBasedArray(int capacity) {
        this.capacity = capacity;
        value = (T[])new Object[capacity];
        count = 0;
        holder = new HashMap<>(capacity);
    }

    public void offer(T object) {
        if (object == null) {
            throw new IllegalArgumentException("this cache container not support null value!");
        }
        Integer index = holder.get(object);
        if (index == null) {
            if (isFull()) {
                removeAndCache(object);
            } else {
                cache(object, count);
            }
        } else {
            update(index);
        }
    }

    /**
     * 若缓存中有指定的值，则更新位置
     *
     * @param end
     */
    public void update(int end) {
        T target = value[end];
        rightShift(end);
        value[0] = target;
        holder.put(target, 0);
    }

    /**
     * 缓存数据到头部，但要先右移
     *
     * @param object
     * @param end
     *            数组右移的边界
     */
    public void cache(T object, int end) {
        rightShift(end);
        value[0] = object;
        holder.put(object, 0);
        count++;
    }

    /**
     * 缓存满的情况，踢出后，再缓存到数组头部
     *
     * @param object
     */
    public void removeAndCache(T object) {
        T key = value[--count];
        holder.remove(key);
        cache(object, count);
    }

    /**
     * end左边的数据统一右移一位
     *
     * @param end
     */
    private void rightShift(int end) {
        for (int i = end - 1; i >= 0; i--) {
            value[i + 1] = value[i];
            holder.put(value[i], i + 1);
        }
    }

    public boolean isContain(T object) {
        return holder.containsKey(object);
    }

    public boolean isEmpty() {
        return count == 0;
    }

    public boolean isFull() {
        return count == capacity;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(value[i]);
            sb.append(" ");
        }
        return sb.toString();
    }

    static class TestLRUBasedArray {

        public static void main(String[] args) {
            // testDefaultConstructor();
            // testSpecifiedConstructor(4);
            // testWithException();
            testString();
        }

        private static void testWithException() {
            LRUBasedArray<Integer> lru = new LRUBasedArray<Integer>();
            lru.offer(null);
        }

        public static void testString() {
            System.out.println("test String cache:::");
            LRUBasedArray<String> lru = new LRUBasedArray<>(3);
            lru.offer("a");
            lru.offer("b");
            lru.offer("c");
            lru.offer("a");
            System.out.println(lru);
        }

        public static void testDefaultConstructor() {
            System.out.println("======无参测试========");
            LRUBasedArray<Integer> lru = new LRUBasedArray<Integer>();
            lru.offer(1);
            lru.offer(2);
            lru.offer(3);
            lru.offer(4);
            lru.offer(5);
            System.out.println(lru);
            lru.offer(6);
            lru.offer(7);
            lru.offer(8);
            lru.offer(9);
            System.out.println(lru);
        }

        public static void testSpecifiedConstructor(int capacity) {
            System.out.println("======有参测试========");
            LRUBasedArray<Integer> lru = new LRUBasedArray<Integer>(capacity);
            lru.offer(1);
            System.out.println(lru);
            lru.offer(2);
            System.out.println(lru);
            lru.offer(3);
            System.out.println(lru);
            lru.offer(4);
            System.out.println(lru);
            lru.offer(2);
            System.out.println(lru);
            lru.offer(4);
            System.out.println(lru);
            lru.offer(7);
            System.out.println(lru);
            lru.offer(1);
            System.out.println(lru);
            lru.offer(2);
            System.out.println(lru);
        }
    }
}
