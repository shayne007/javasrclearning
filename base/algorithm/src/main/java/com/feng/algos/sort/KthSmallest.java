package com.feng.algos.sort;

/**
 * @author fengsy
 * @date 6/16/21
 * @Description
 */
public class KthSmallest {
    public static int kthSmallest(int[] arr, int k) {
        if (arr == null || arr.length < k) {
            return -1;
        }
        int partition = internalPartition(arr, 0, arr.length - 1, k);
        return arr[partition];
    }

    private static int internalPartition(int[] arr, int p, int r, int k) {
        int partition = partition(arr, p, r);
        if (partition + 1 == k) {
            return partition;
        }
        if (partition + 1 < k) {
            return internalPartition(arr, partition + 1, r, k);
        } else {
            return internalPartition(arr, 0, partition - 1, k);
        }
    }

    private static int partition(int[] arr, int p, int r) {
        int pivot = arr[r];

        int i = p;
        for (int j = p; j < r; j++) {
            // 这里要是 <= ，不然会出现死循环，比如查找数组 [1,1,2] 的第二小的元素
            if (arr[j] <= pivot) {
                swap(arr, i, j);
                i++;
            }
        }
        swap(arr, i, r);
        return i;
    }

    private static void swap(int[] arr, int i, int j) {
        if (i == j) {
            return;
        }

        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    public static void main(String[] args) {
        int[] array = new int[] {9, 4, 11, 1, 5, 6, 7, 8};
        System.out.println(kthSmallest(array, 3));
    }
}
