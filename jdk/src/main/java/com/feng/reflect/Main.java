package com.feng.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author fengsy
 * @date 1/19/21
 * @Description
 */

public class Main {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException,
        InstantiationException, InvocationTargetException, NoSuchFieldException {
        /**
         * 获取TargetClass类的Class对象并且创建TargetClass类实例
         */
        Class<?> tagetClass = Class.forName("com.feng.reflect.TargetClass");
        // Class tagetClass = ClassLoader.getSystemClassLoader().loadClass("com.fsy.javasrc.reflect.TargetClass");
        System.out.println("-------get class before instance-----");
        TargetClass targetObject = (TargetClass)tagetClass.newInstance();
        /**
         * 获取所有类中所有定义的方法
         */
        Method[] methods = tagetClass.getDeclaredMethods();
        for (Method method : methods) {
            System.out.println(method.getName());
        }
        /**
         * 获取指定方法并调用
         */
        Method publicMethod = tagetClass.getMethod("publicMethod", String.class);

        publicMethod.invoke(targetObject, "new str");
        /**
         * 获取指定参数并对参数进行修改
         */
        Field field = tagetClass.getDeclaredField("value");
        // 为了对类中的参数进行修改我们取消安全检查
        field.setAccessible(true);
        field.set(targetObject, "reflect");
        /**
         * 调用 private 方法
         */
        Method privateMethod = tagetClass.getDeclaredMethod("privateMethod");
        // 为了调用private方法我们取消安全检查
        privateMethod.setAccessible(true);
        privateMethod.invoke(targetObject);
    }
}
