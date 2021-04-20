package com.feng.javasrc.reflect;

/**
 * @author fengsy
 * @date 1/19/21
 * @Description
 */
public class TargetClass {
    private String value;

    static {
        System.out.println("initial...");
    }

    public TargetClass() {
        value = "JavaGuide";
    }

    public void publicMethod(String s) {
        System.out.println("I love " + s);
    }

    private void privateMethod() {
        System.out.println("value is " + value);
    }
}