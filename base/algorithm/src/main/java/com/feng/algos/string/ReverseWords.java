package com.feng.algos.string;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * 翻转字符串里的单词
 * 
 * @author fengsy
 * @date 6/22/21
 * @Description
 */
public class ReverseWords {

    public static String reverseWords(String s) {
        String a = s.trim();
        String[] words = a.split("\\s+");

        for (int left = 0, right = words.length - 1; left < right; ++left, --right) {
            String tmp = words[left];
            words[left] = words[right];
            words[right] = tmp;
        }
        return Arrays.stream(words).collect(Collectors.joining(" "));
    }

    public String reverseWords2(String s) {
        if (s == null || s.length() == 0)
            return null;
        String[] res = s.split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (int i = res.length - 1; i >= 0; i--) {
            sb.append(res[i]);
            sb.append(" ");
        }
        return sb.toString().trim();
    }

    public static void main(String[] args) {
        String s = "ee a r";
        System.out.println(reverseWords(s));
    }
}
