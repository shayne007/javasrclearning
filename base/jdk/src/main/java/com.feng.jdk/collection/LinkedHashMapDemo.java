package com.feng.collection;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author fengsy
 * @date 1/20/21
 * @Description
 */
public class LinkedHashMapDemo {

    public static void main(String[] args) {
        HashMap<String, Integer> h = new HashMap<>(33);
        h.put("one", 1);
        h.put("two", 2);
        h.put("three", 3);
        h.put("four", 4);
        h.put("five", 5);
        h.put("onf", 5);
        for (String key : h.keySet()) {
            System.out.println("key:" + key + "\t value:" + h.get(key));
        }

        System.out.println("=========================");
        LinkedHashMap<String, Integer> lh = new LinkedHashMap<>(33);
        lh.put("one", 1);
        lh.put("two", 2);
        lh.put("three", 3);
        lh.put("four", 4);
        for (String key : lh.keySet()) {
            System.out.println("key:" + key + "\t value:" + lh.get(key));
        }
        System.out.println("=========================");
        // 10是初始大小，0.75是装载因子，true是表示按照访问时间排序
        HashMap<Integer, Integer> m = new LinkedHashMap<>(10, 0.75f, true);
        m.put(3, 11);
        m.put(1, 12);
        m.put(5, 23);
        m.put(2, 22);

        m.put(3, 26);
        m.get(5);

        for (Map.Entry e : m.entrySet()) {
            System.out.println("key:" + e.getKey() + ", value:" + e.getValue());
        }
        m.forEach((Integer k, Integer v) -> {
            System.out.println("key:" + k + ", value:" + v);
        });

        // accessOrder 为 true 时，为了防止并发条件下，遍历的同时链表发生变化，ConcurrentModificationException
        for (Integer integer : m.keySet()) {
            System.out.println(m.get(integer));
        }

    }
}
