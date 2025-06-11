package com.feng.algos.other;

import java.util.Arrays;

/**
 * @Description leecode 410. 分割数组的最大值
 * @Author fengsy
 * @Date 12/8/21
 */
public class BestEfficience {

    // 动态规划
    public static int splitArray(int[] nums, int m) {
        int n = nums.length;
        //初始化动规的初始值为最大整数
        int[][] f = new int[n + 1][m + 1];
        for (int i = 0; i <= n; i++) {
            Arrays.fill(f[i], Integer.MAX_VALUE);
        }
        //求出nums数组的所有前缀和
        int[] preSum = new int[n + 1];
        for (int i = 0; i < n; i++) {
            preSum[i + 1] = preSum[i] + nums[i];
        }

        f[0][0] = 0;

        //根据状态转移方程 计算f[i][j]
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= Math.min(i, m); j++) {
                for (int k = 0; k < i; k++) {
                    f[i][j] = Math.min(f[i][j], Math.max(f[k][j - 1], preSum[i] - preSum[k]));
                }
            }
        }
        return f[n][m];
    }

    /**
     * 方法二：二分查找 + 贪心
     * 二分的上界为数组nums 中所有元素的和，下界为数组nums 中所有元素的最大值。通过二分查找，我们可以得到最小的最大分割子数组和
     *
     * @param nums
     * @param m
     * @return
     */
    public static int splitArray2(int[] nums, int m) {
        int left = 0, right = 0;
        for (int i = 0; i < nums.length; i++) {
            right += nums[i];
            if (left < nums[i]) {
                left = nums[i];
            }
        }
        while (left < right) {
            int mid = (right - left) / 2 + left;
            if (check(nums, mid, m)) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return left;
    }

    public static boolean check(int[] nums, int x, int m) {
        int sum = 0;
        int cnt = 1;
        for (int i = 0; i < nums.length; i++) {
            if (sum + nums[i] > x) {
                cnt++;
                sum = nums[i];
            } else {
                sum += nums[i];
            }
        }
        return cnt <= m;
    }


    public static void main(String[] args) {
        int[] arr = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        System.out.println(splitArray(arr, 5));
        System.out.println(splitArray2(arr, 5));
    }
}
