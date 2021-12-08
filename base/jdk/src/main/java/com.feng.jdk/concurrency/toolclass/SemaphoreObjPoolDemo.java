package com.feng.jdk.concurrency;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.Semaphore;
import java.util.function.Function;

// import sun.misc.Unsafe;

/**
 * @author fengsy
 * @date 1/14/21
 * @Description
 */

class ObjPool<T, R> {
    static final boolean is64bit = true;

    final List<T> pool;
    // 用信号量实现限流器
    final Semaphore sem;

    // 构造函数
    ObjPool(int size, T t) {
        pool = new Vector<T>(size) {
        };
        for (int i = 0; i < size; i++) {
            pool.add(t);
        }
        sem = new Semaphore(size);
    }

    // 利用对象池的对象，调用func
    R exec(Function<T, R> func) {
        T t = null;
        try {
            sem.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            t = pool.remove(0);
            return func.apply(t);
        } finally {
            pool.add(t);
            sem.release();
        }
    }

    public static void main(String[] args) {
        // 创建对象池
        ObjPool<Long, String> pool = new ObjPool<Long, String>(10, new Long(23));
        // 通过对象池获取t，之后执行

        for (int i = 0; i < 20; i++) {
            Thread th = new Thread(() -> {
                pool.exec(t -> {
                    System.out.println(t);
                    printAddresses("Address", t);
                    return t.toString();
                });
            });

            th.start();
        }

    }

    public static Unsafe createUnsafe() {
        try {
            Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
            Field field = unsafeClass.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            Unsafe unsafe = (Unsafe) field.get(null);
            return unsafe;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void printAddresses(String label, Object... objects) {
        System.out.print(label + ":         0x");
        long last = 0;
        int offset = createUnsafe().arrayBaseOffset(objects.getClass());
        int scale = createUnsafe().arrayIndexScale(objects.getClass());
        switch (scale) {
            case 4:
                long factor = is64bit ? 8 : 1;
                final long i1 = (createUnsafe().getInt(objects, offset) & 0xFFFFFFFFL) * factor;
                System.out.println(Long.toHexString(i1));
                last = i1;
                for (int i = 1; i < objects.length; i++) {
                    final long i2 = (createUnsafe().getInt(objects, offset + i * 4) & 0xFFFFFFFFL) * factor;
                    if (i2 > last)
                        System.out.print(", +" + Long.toHexString(i2 - last));
                    else
                        System.out.print(", -" + Long.toHexString(last - i2));
                    last = i2;
                }
                break;
            case 8:
                throw new AssertionError("Not supported");
        }
        System.out.println();
    }
}
