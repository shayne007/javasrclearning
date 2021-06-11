package com.feng.collection;

import java.util.ArrayList;
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
        ArrayList<String> list = new ArrayList<String>();
        list.add("a");
        list.add("a");
        list.add("b");
        list.add("b");
        list.add("c");
        list.add("c");
        remove(list);// 删除指定的“b”元素
        System.out.println(list);
        for (int i = 0; i < list.size(); i++) {
            System.out.println("element : " + list.get(i));
        }

        List syncList = Collections.synchronizedList(list);
        synchronized (syncList) {
            Iterator<String> it = syncList.iterator();
            it.forEachRemaining(s -> System.out.println(s));
        }
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
