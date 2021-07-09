package com.feng.aop.proxyAttributeUnreachable;

import sun.reflect.ReflectionFactory;

import java.lang.reflect.Constructor;

/**
 * java.lang.Class.newInsance() <br>
 * java.lang.reflect.Constructor.newInstance()<br>
 * sun.reflect.ReflectionFactory.newConstructorForSerialization().newInstance()
 *
 * @author fengsy
 * @date 7/9/21
 * @Description
 */

public class TestNewInstanceStyle {

    public static class User {
        public String name = "fujian";
    }

    public static void main(String[] args) throws Exception {
        // ReflectionFactory.newConstructorForSerialization()方式
        ReflectionFactory reflectionFactory = ReflectionFactory.getReflectionFactory();
        Constructor constructor =
                reflectionFactory.newConstructorForSerialization(User.class, Object.class.getDeclaredConstructor());
        constructor.setAccessible(true);
        User testObject1 = (User) constructor.newInstance();
        System.out.println(testObject1.name);
        // 普通方式
        User testObject2 = new User();
        System.out.println(testObject2.name);
    }

}