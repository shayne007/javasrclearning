package com.feng.jdk.oop;

/**
 * @author fengsy
 * @date 6/10/21
 * @Description
 */

class Parent {
    public static String parentStr = "parent static string";

    static {
        System.out.println("parent static fields");
        System.out.println(parentStr);
    }

    public Parent() {
        System.out.println("parent instance initialization");
    }
}

public class TestSubclassInit extends Parent {
    public static String subStr = "sub static string";

    static {
        System.out.println("sub static fields");
        System.out.println(subStr);
    }

    public TestSubclassInit() {
        System.out.println("sub instance initialization");
    }

    public static void main(String[] args) {
        System.out.println("sub main");
        new TestSubclassInit();
    }
}