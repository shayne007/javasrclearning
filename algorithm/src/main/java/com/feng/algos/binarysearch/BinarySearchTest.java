package com.feng.algos.binarysearch;

import java.util.Arrays;

/**
 * @author fengsy
 * @date 6/17/21
 * @Description
 */
public class BinarySearchTest {
    /**
     * 查找第一个值等于给定值的元素
     *
     * @param a
     * @param n
     * @param value
     * @return
     */
    public int bsearchFirstEqual(int[] a, int n, int value) {
        int low = 0;
        int high = n - 1;
        while (low <= high) {
            int mid = low + ((high - low) >> 1);
            if (a[mid] > value) {
                high = mid - 1;
            } else if (a[mid] < value) {
                low = mid + 1;
            } else {
                if ((mid == 0) || (a[mid - 1] != value))
                    return mid;
                else
                    high = mid - 1;
            }
        }
        return -1;
    }

    /**
     * 查找最后一个值等于给定值的元素
     *
     * @param a
     * @param n
     * @param value
     * @return
     */
    public int bsearchLastEqual(int[] a, int n, int value) {
        int low = 0;
        int high = n - 1;
        while (low <= high) {
            int mid = low + ((high - low) >> 1);
            if (a[mid] > value) {
                high = mid - 1;
            } else if (a[mid] < value) {
                low = mid + 1;
            } else {
                if ((mid == n - 1) || (a[mid + 1] != value))
                    return mid;
                else
                    low = mid + 1;
            }
        }
        return -1;
    }

    /**
     * 查找第一个大于等于给定值的元素
     *
     * @param a
     * @param n
     * @param value
     * @return
     */
    public int bsearchFirstGreaterEqual(int[] a, int n, int value) {
        int low = 0;
        int high = n - 1;
        while (low <= high) {
            int mid = low + ((high - low) >> 1);
            if (a[mid] >= value) {
                if ((mid == 0) || (a[mid - 1] < value))
                    return mid;
                else
                    high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return -1;
    }

    /**
     * 查找最后小于等于给定值的元素
     *
     * @param a
     * @param n
     * @param value
     * @return
     */
    public int bsearchFirstLessEqual(int[] a, int n, int value) {
        int low = 0;
        int high = n - 1;
        while (low <= high) {
            int mid = low + ((high - low) >> 1);
            if (a[mid] > value) {
                high = mid - 1;
            } else {
                if ((mid == n - 1) || (a[mid + 1] > value))
                    return mid;
                else
                    low = mid + 1;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        int[] array = new int[]{3, 4, 2, 1, 5, 6, 7, 8};
        Arrays.sort(array);
        System.out.println(Arrays.toString(array));
        int index = Arrays.binarySearch(array, 99);
        System.out.println(index);
    }
}
