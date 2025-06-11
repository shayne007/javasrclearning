package net.mindview.util;

import java.util.AbstractList;

public class CountingIntegerList extends AbstractList<Integer> {
    private int size;
    public CountingIntegerList(int size) {
        this.size = size;
    }
    public Integer get(int index) {
        return index;
    }
    public int size() {
        return size;
    }
} 