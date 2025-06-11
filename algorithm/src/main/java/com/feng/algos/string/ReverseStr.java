package com.feng.algos.string;

/**
 * 字符串反转
 *
 * @author fengsy
 * @date 6/22/21
 * @Description
 */
public class ReverseStr {
    public static void reverseString(char[] s) {
        int n = s.length;
        for (int left = 0, right = n - 1; left < right; ++left, --right) {
            char tmp = s[left];
            s[left] = s[right];
            s[right] = tmp;
        }
    }

    public static void main(String[] args) {
        char[] strs = {'a', 'b', 'c'};
        reverseString(strs);
        System.out.println((strs));
    }
}
