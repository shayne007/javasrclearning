package com.feng.jdk.concurrency.toolclass;


import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * @Description Fork/Join：单机版的MapReduce
 * 分而治之的思想；
 * @Author fengsy
 * @Date 11/8/21
 */
public class ForkJoinMergeSortDemo {
    public static void main(String[] args) {
        long[] arrs = new long[100_000_000];
        for (int i = 0; i < 100_000_000; i++) {
            arrs[i] = (long) (Math.random() * 100_000_000);
        }
        long startTime = System.currentTimeMillis();
        ForkJoinPool forkJoinPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
        MergeSortTask mergeSortTask = new MergeSortTask(arrs);
        //forkJoin实现递归
        arrs = forkJoinPool.invoke(mergeSortTask);
        //传统递归
//        arrs = mergeSort(arrs);
        long endTime = System.currentTimeMillis();
        System.out.println("耗时：" + (endTime - startTime));
    }

    /**
     * 传统递归
     * 耗时：30508ms
     */
    private static long[] mergeSort(long[] arrs) {
        if (arrs.length < 2) return arrs;
        int mid = arrs.length / 2;
        long[] left = Arrays.copyOfRange(arrs, 0, mid);
        long[] right = Arrays.copyOfRange(arrs, mid, arrs.length);
        return merge(mergeSort(left), mergeSort(right));
    }

    private static long[] merge(long[] left, long[] right) {
        long[] result = new long[left.length + right.length];
        for (int i = 0, m = 0, j = 0; m < result.length; m++) {
            if (i >= left.length) {
                result[m] = right[j++];
            } else if (j >= right.length) {
                result[m] = left[i++];
            } else if (left[i] > right[j]) {
                result[m] = right[j++];
            } else result[m] = left[i++];
        }
        return result;
    }


    static class MergeSortTask extends RecursiveTask<long[]> {
        long[] arrs;

        public MergeSortTask(long[] arrs) {
            this.arrs = arrs;
        }

        @Override
        protected long[] compute() {
            if (arrs.length < 2) return arrs;
            int mid = arrs.length / 2;
            MergeSortTask left = new MergeSortTask(Arrays.copyOfRange(arrs, 0, mid));
            left.fork();
            MergeSortTask right = new MergeSortTask(Arrays.copyOfRange(arrs, mid, arrs.length));
            return merge(right.compute(), left.join());
        }

        private long[] merge(long[] left, long[] right) {
            long[] result = new long[left.length + right.length];
            for (int i = 0, m = 0, j = 0; m < result.length; m++) {
                if (i >= left.length) {
                    result[m] = right[j++];
                } else if (j >= right.length) {
                    result[m] = left[i++];
                } else if (left[i] > right[j]) {
                    result[m] = right[j++];
                } else result[m] = left[i++];
            }
            return result;
        }
    }
}