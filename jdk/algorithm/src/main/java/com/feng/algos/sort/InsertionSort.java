package com.feng.algos.sort;

import java.util.Arrays;

/**
 * @author fengsy
 * @date 6/16/21
 * @Description
 */
public class InsertionSort {
    // 插入排序，a表示数组，n表示数组大小
    public static void insertionSort(int[] a, int n) {
        if (n <= 1)
            return;

        for (int i = 1; i < n; ++i) {
            int value = a[i];
            int j = i - 1;
            // 查找要插入的位置并移动数据
            for (; j >= 0; --j) {
                if (a[j] > value) {
                    a[j + 1] = a[j];
                } else {
                    break;
                }
            }
            a[j + 1] = value;// j+1处的空位插入数据
        }
    }

    /**
     * 查询插入位置时， 从头至尾搜索
     * 
     * @param data
     */
    private static int[] fromStartToEnd(int[] data) {
        for (int i = 1; i < data.length; i++) {
            int value = data[i];

            int[] tmp = new int[2];
            int change = i;
            for (int j = 0; j < i; j++) {
                if (value >= data[j]) {
                    continue;
                }

                int index = j % 2;
                if (change == i) {
                    tmp[Math.abs(index - 1)] = data[j];
                    change = j;
                }
                tmp[index] = data[j + 1];
                if (0 == index) {
                    data[j + 1] = tmp[index + 1];
                } else {
                    data[j + 1] = tmp[index - 1];
                }
            }
            data[change] = value;
        }
        return data;
    }

    public static void main(String[] args) {
        int[] data = new int[] {4, 6, 5, 3, 7, 1, 2};
        int[] sorted = fromStartToEnd(Arrays.copyOf(data, data.length));
        System.out.println(Arrays.toString(data));
        System.out.println(Arrays.toString(sorted));
    }
}
