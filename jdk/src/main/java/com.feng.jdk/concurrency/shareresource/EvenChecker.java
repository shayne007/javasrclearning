package com.feng.jdk.concurrency.shareresource;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author fengsy
 * @date 5/12/21
 * @Description
 */
public class EvenChecker implements Runnable {
    private IntGenerator generator;
    private final int id;

    public EvenChecker(IntGenerator g, int ident) {
        generator = g;
        id = ident;
    }

    /**
     * <p>
     * 循环中多个线程同时运行各自获取generator生成的下一个偶数，判断获取到不是偶数时停止
     * </p>
     * generator作为共享资源被多个线程公用，共享资源：cancel状态，next()方法不是安全的原子操作
     * <p>
     * by making tasks depend on a nontask object, we eliminate the potential race condition.
     * </p>
     */
    @Override
    public void run() {
        while (!generator.isCanceled()) {
            int val = generator.next();
            if (val % 2 != 0) {
                System.out.println(Thread.currentThread().getName() + " : " + val + " not even!");
                // Cancels all EvenCheckers
                generator.cancel();
            }
        }
    }

    public static void test(IntGenerator intGenerator, int count) {
        System.out.println("Press Control-C to exit");
        ExecutorService exec = Executors.newCachedThreadPool();

        for (int i = 0; i < count; i++) {
            exec.execute(new EvenChecker(intGenerator, i));
        }
        exec.shutdown();
    }

    public static void test(IntGenerator gp) {
        test(gp, 10);
    }
}
