package com.feng.javasrc.collections;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author fengsy
 * @date 1/21/21
 * @Description
 */
public class HashMapTest {
    public static void main(String[] args) {
        // 创建并赋值 HashMap
        Map<Integer, String> map = new HashMap();
        map.put(1, "Java");
        map.put(2, "JDK");
        map.put(3, "Spring Framework");
        map.put(4, "MyBatis framework");
        map.put(5, "Java中文社群");

        // 1.iterator keyset
        Iterator<Integer> iterator = map.keySet().iterator();
        while (iterator.hasNext()) {
            Integer key = iterator.next();
            if (key == 2) {
                iterator.remove();
                continue;
            }
            System.out.print(key);
            System.out.println(map.get(key));
        }
        // 2.foreach entryset
        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            if (entry.getKey() == 2) {
                map.remove(entry.getKey());
                continue;
            }
            System.out.print(entry.getKey());
            System.out.println(entry.getValue());
        }

        // 3.foreach keyset
        for (Integer key : map.keySet()) {
            if (key == 2) {
                map.remove(key);
                continue;
            }
            System.out.print(key);
            System.out.println(map.get(key));
        }

        // 4.lambda
        map.forEach((key, value) -> {
            if (key == 2) {
                map.remove(key);
            }
            System.out.print(key);
            System.out.println(value);
        });

        // 5.stream api
        map.entrySet().stream().forEach(entry -> {
            if (entry.getKey() == 2) {
                map.remove(entry.getKey());
            }
            System.out.print(entry.getKey());
            System.out.println(entry.getValue());
        });

        // 5.stream api parallel
        map.entrySet().parallelStream().forEach(entry -> {
            System.out.print(entry.getKey());
            System.out.println(entry.getValue());
        });
    }
}
