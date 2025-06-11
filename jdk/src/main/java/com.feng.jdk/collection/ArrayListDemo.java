package com.feng.jdk.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author fengsy
 * @date 6/7/21
 * @Description
 */
public class ArrayListDemo {

    public static void main(String[] args) {
        Arrays.asList("1", 2);
        ArrayList<String> list = new ArrayList<String>(10);
        list.add("a");
        list.add("a");
        list.add("b");
        list.add("b");
        list.add("c");
        list.add("c");
        remove(list);// 删除指定的"b"元素
        System.out.println(list);
        // Using a simpler approach to estimate size
        System.out.println("Estimated size: " + estimateSize(list));
        for (int i = 0; i < list.size(); i++) {
            System.out.println("element : " + list.get(i));
        }

        List syncList = Collections.synchronizedList(list);
        synchronized (syncList) {
            Iterator<String> it = syncList.iterator();
            it.forEachRemaining(s -> System.out.println(s));
        }
    }

    private static long estimateSize(ArrayList<?> list) {
        // Rough estimation: 16 bytes for ArrayList object header
        // + 4 bytes for size field
        // + 4 bytes for modCount field
        // + 4 bytes for elementData reference
        // + 4 bytes for each element reference
        // + 4 bytes for each String object reference
        return 16 + 4 + 4 + 4 + (list.size() * 8);
    }

    public static void remove(ArrayList<String> list) {
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            String str = it.next();
            if (str.equals("b")) {
                it.remove();
            }
        }
    }

    /**
     * throw ConcurrentModifyException
     *
     * @param list
     */
    public static void remove2(ArrayList<String> list) {
        for (String s : list) {
            if (s.equals("b")) {
                list.remove(s);
            }
        }
    }
}
