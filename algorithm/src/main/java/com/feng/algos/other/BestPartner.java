package com.feng.algos.other;

import java.util.Arrays;

/**
 * @Description leecode 209. 长度最小的子数组
 * @Author fengsy
 * @Date 12/7/21
 */
public class BestPartner {
    /**
     * @param sum  任务的总工作量，例如： int sum = 7
     * @param nums 每个员工的产能，例如： int[] nums = [3,2,2,1,2,5]
     * @return 相邻的、最少的参与人数，例如： count = 2（相邻元素为：[2,5]）
     */
    public static int min(int sum, int[] nums) {
        int count = 0;
        for (int i = 1; i <= nums.length; i++) {
            for (int j = 0; i + j <= nums.length; j++) {
                int tempSum = 0;
                for (int k = j; k < i + j; k++) {
                    tempSum += nums[k];
                }
                if (tempSum == sum) {
                    count = i;
//                    for (int k = j; k < i + j; k++) {
//                        System.out.print(nums[k] + " ");
//                    }
                    return count;
                }
            }
        }
        return count;
    }

    public static int minArrayLen(int sum, int[] nums) {
        int count = 0;
        int result = 0;
        for (int i = 0; i < nums.length; i++) {
            int tempSum = 0;
            int start = i;
            while (tempSum < sum && i < nums.length) {
                tempSum += nums[i];
                i++;
                count = i - start;
            }
            i = start;
            if (i == 0) {
                result = count;
            }
            if (tempSum >= sum) {
                result = result < count ? result : count;
            }
        }
        return result;
    }

    /**
     * 方法一：暴力法
     * 暴力法是最直观的方法。初始化子数组的最小长度为无穷大， 枚举数组 nums 中的每个下标作为子数组的开始下标，
     * 对于每个开始下标 i，需要找到大于或等于 i 的最小下标 j， 使得从 nums[i] 到nums[j] 的元素和大于或等于s，
     * 并更新子数组的最小长度（此时子数组的长度是 j-i+1）
     *
     * @param s
     * @param nums
     * @return
     */
    public static int minSubArrayLen(int s, int[] nums) {
        int n = nums.length;
        if (n == 0) {
            return 0;
        }
        int result = Integer.MAX_VALUE;
        for (int i = 0; i < n; i++) {
            int sum = 0;
            for (int j = i; j < n; j++) {
                sum += nums[j];
                if (sum >= s) {
                    result = Math.min(result, j - i + 1);
                    break;
                }
            }
        }
        return result == Integer.MAX_VALUE ? 0 : result;
    }


    /**
     * 前缀和 + 二分查找
     *
     * @param s
     * @param nums
     * @return
     */
    public static int minSubArrayLen2(int s, int[] nums) {
        int n = nums.length;
        if (n == 0) {
            return 0;
        }
        int ans = Integer.MAX_VALUE;
        int[] sums = new int[n + 1];
        // 为了方便计算，令 size = n + 1
        // sums[0] = 0 意味着前 0 个元素的前缀和为 0
        // sums[1] = A[0] 前 1 个元素的前缀和为 A[0]
        // 以此类推
        for (int i = 1; i <= n; i++) {
            sums[i] = sums[i - 1] + nums[i - 1];
        }
        for (int i = 1; i <= n; i++) {
            int target = s + sums[i - 1];
            int bound = Arrays.binarySearch(sums, target);
            if (bound < 0) {
                bound = -bound - 1;
            }
            if (bound <= n) {
                ans = Math.min(ans, bound - (i - 1));
            }
        }
        return ans == Integer.MAX_VALUE ? 0 : ans;
    }

    public static void main(String[] args) {
        int[] data = new int[]{3, 2, 2, 1, 2, 2, 2, 1, 2, 2, 2, 1, 2, 5};

        long start = System.currentTimeMillis();
        for (int i = 0; i < 10_000_000; i++) {
            min(10, data);
        }
        System.out.println(System.currentTimeMillis() - start);

        start = System.currentTimeMillis();
        for (int i = 0; i < 10_000_000; i++) {
            minArrayLen(10, data);
        }
        System.out.println(System.currentTimeMillis() - start);

        start = System.currentTimeMillis();
        for (int i = 0; i < 10_000_000; i++) {
            minSubArrayLen(10, data);
        }
        System.out.println(System.currentTimeMillis() - start);

        System.out.println(min(10, data));
        System.out.println(minArrayLen(10, data));
        System.out.println(minSubArrayLen(10, data));
        System.out.println(minSubArrayLen2(10, data));
    }
}
