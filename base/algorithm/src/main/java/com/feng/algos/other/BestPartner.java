package com.feng.algos.other;

/**
 * @Description 最佳拍档
 * @Author fengsy
 * @Date 12/7/21
 */
public class BestPartnalnal {
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

        int result = min(10, data);
        int result2 = minArrayLen(10, data);
        System.out.println(result);
        System.out.println(result2);
    }
}
