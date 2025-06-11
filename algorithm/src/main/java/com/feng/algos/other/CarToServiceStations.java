package com.feng.algos.other;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @Description Leecode 821. 字符的最短距离，不区分大小写
 * @Author fengsy
 * @Date 12/7/21
 */
public class CarToServiceStations {
    /**
     * 执行用时：11 ms
     * 内存消耗： 39 MB
     *
     * @param s
     * @param c
     * @return
     */
    public static int[] minDistance(String s, char c) {
        char[] chars = s.toCharArray();
        if (chars.length <= 0) return new int[0];

        int[] result = new int[chars.length];
        Set<Integer> indexs = new HashSet();
        for (int i = 0; i < chars.length; i++) {
            int distance = Math.abs(chars[i] - c);
            if (distance == 0 || distance == 32) {
                indexs.add(i);
            }
        }
        if (indexs.isEmpty()) {
            return new int[0];
        }
        for (int i = 0; i < result.length; i++) {
            int finalI = i;
            result[i] = indexs.stream().mapToInt(index -> Math.abs(finalI - index)).min().getAsInt();
        }
        return result;
    }

    /**
     * 方法 1：最小数组
     * 想法:
     * 对于每个字符 s[i]，试图找出距离向左或者向右下一个字符 c 的距离。答案就是这两个值的较小值。
     * 算法:
     * 从左向右遍历，记录上一个字符 c 出现的位置 prev，那么答案就是 i - prev。
     * 从右向左遍历，记录上一个字符 c 出现的位置 prev，那么答案就是 prev - i。
     * 这两个值取最小就是答案。
     *
     * @param s
     * @param c
     * @return
     */
    public static int[] shortestToChar(String s, char c) {
        int n = s.length();
        int[] result = new int[n];
        int prev = Integer.MIN_VALUE / 2;

        for (int i = 0; i < n; ++i) {
            if (s.charAt(i) == c || Math.abs(s.charAt(i) - c) == 32) prev = i;
            result[i] = i - prev;
        }

        prev = Integer.MAX_VALUE / 2;
        for (int i = n - 1; i >= 0; --i) {
            if (s.charAt(i) == c || Math.abs(s.charAt(i) - c) == 32) prev = i;
            result[i] = Math.min(result[i], prev - i);
        }
        return result;
    }

    public static void main(String[] args) {

        int[] result = minDistance("newCoder", 'c');
        int[] result2 = shortestToChar("newCoder", 'c');

        Arrays.stream(result).forEach(i -> {
            System.out.print(" " + i);
        });

        System.out.println();
        Arrays.stream(result2).forEach(i -> {
            System.out.print(" " + i);
        });
    }

}
