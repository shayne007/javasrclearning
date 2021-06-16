package com.feng.queue;

import java.util.Deque;
import java.util.LinkedList;

/**
 * @author fengsy
 * @date 6/15/21
 * @Description
 */
public class IntervalOrderedQueue {

    private static int MAX_N = 10;
    static int[] q = new int[MAX_N + 5];
    static int head, tail;

    static void interval_max_number(int[] a, int n, int m) {
        head = tail = 0;
        for (int i = 0; i < n; i++) {
            // a[i] 入队，将违反单调性的从队列 q 中踢出
            while (head < tail && a[q[tail - 1]] < a[i])
                tail--;
            q[tail++] = i; // i 入队
            // 判断队列头部元素是否出了窗口范围
            if (i - m == q[head])
                head++;
            // 输出区间内最大值
            if (i + 1 >= m) {
                System.out.printf("interval(%d, %d)", i - m + 1, i);
                System.out.printf(" = %d\n", a[q[head]]);
            }
        }
        return;
    }

    public static int[] maxSlidingWindow(int[] nums, int k) {
        int n = nums.length;
        Deque<Integer> deque = new LinkedList<>();
        for (int i = 0; i < k; ++i) {
            while (!deque.isEmpty() && nums[i] >= nums[deque.peekLast()]) {
                deque.pollLast();
            }
            deque.offerLast(i);
        }

        int[] ans = new int[n - k + 1];
        ans[0] = nums[deque.peekFirst()];
        for (int i = k; i < n; ++i) {
            while (!deque.isEmpty() && nums[i] >= nums[deque.peekLast()]) {
                deque.pollLast();
            }
            deque.offerLast(i);
            while (deque.peekFirst() <= i - k) {
                deque.pollFirst();
            }
            ans[i - k + 1] = nums[deque.peekFirst()];
        }
        return ans;
    }

    public static void main(String[] args) {
        int[] arr = {6, 4, 20, 10, 3, 8, 5, 9, 22, 7};
        // interval_max_number(arr, 10, 3);
        int[] result = maxSlidingWindow(arr, 3);
        for (int i = 0; i < result.length; i++) {
            System.out.print(result[i] + ",");
        }
    }
}
