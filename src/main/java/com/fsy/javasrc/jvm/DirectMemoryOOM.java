package com.fsy.javasrc.jvm;

// import sun.misc.Unsafe;

import java.lang.reflect.Field;

import sun.misc.Unsafe;

/**
 * @author fengsy
 * @date 1/26/21
 * @Description
 */
public class DirectMemoryOOM {

    private static final int _1MB = 2024 * 2024;

    public static void main(String[] args) throws Exception {
        Field unsafeField = Unsafe.class.getDeclaredFields()[0];
        unsafeField.setAccessible(true);
        Unsafe unsafe = (Unsafe)unsafeField.get(null);
        while (true) {
            unsafe.allocateMemory(_1MB);
            System.out.println(1);
        }

    }
}