package com.feng.jdk.collection;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * @author fengsy
 * @date 1/20/21
 * @Description
 */
public class TreeSetDemo {
    public static void main(String[] args) {
        Set<String> datesSet = new HashSet<>();
        datesSet.add("11");
        datesSet.add("20");
        datesSet.add("20");
        datesSet.add("3");
        datesSet.add("4");
        datesSet.add("4");
        Set<String> sortSet = new TreeSet<String>(Comparator.reverseOrder());
        sortSet.addAll(datesSet);
        List<String> daysList = sortSet.stream().collect(Collectors.toList());
        for (int i = 0; i < daysList.size(); i++) {
            String day = daysList.get(i);
            System.out.println(day);
        }

    }
}
