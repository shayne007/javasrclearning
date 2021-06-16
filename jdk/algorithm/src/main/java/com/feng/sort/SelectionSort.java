package com.feng.sort;

/**
 * @author fengsy
 * @date 6/16/21
 * @Description
 */
public class SelectionSort {
    // 选择排序，a表示数组，n表示数组大小
    public static void selectionSort(int[] a, int n) {
        if (n <= 1)
            return;

        for (int i = 0; i < n - 1; ++i) {
            // 查找最小值
            int minIndex = i;
            for (int j = i + 1; j < n; ++j) {
                if (a[j] < a[minIndex]) {
                    minIndex = j;
                }
            }

            // 交换
            int tmp = a[i];
            a[i] = a[minIndex];
            a[minIndex] = tmp;
        }
    }
}
