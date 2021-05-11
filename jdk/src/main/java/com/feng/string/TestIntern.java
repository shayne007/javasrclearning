package com.feng.string;

/**
 * @author fengsy
 * @date 2/21/21
 * @Description
 */
public class TestIntern {

    public static void main(String[] args) {
        String s1 = new String("hello") + new String("world");
        System.out.println(s1.intern() == s1);
        System.out.println(s1 == "helloworld");
    }
}
