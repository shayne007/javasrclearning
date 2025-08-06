package com.feng.jdk.concurrency.patterns.tspecstorage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.feng.jdk.tspecstorage.Stu;

/**
 * 直接使用 InheritableThreadLocal 传递对象的引用，变量在线程间共享，有线程安全问题
 *
 * @author fengsy
 * @date 5/18/21
 * @Description
 */
public class InheritableThreadLocalTest {

    public static final ThreadLocal<Stu> threadlocal = new InheritableThreadLocal<>();
//    public static final ThreadLocal<Stu> threadlocal = new MyInheritableThreadLocal<>();

    public static ExecutorService executorService = Executors.newFixedThreadPool(1);

    public static void main(String[] args) throws InterruptedException {
        threadlocal.set(new Stu("zhangsan", 22));
        TimeUnit.SECONDS.sleep(1);
        executorService.submit(() -> {
            System.out.println("子线程变更前:" + threadlocal.get());
            threadlocal.get().setAge(11);
            System.out.println("子线程变更后:" + threadlocal.get());
        });
        TimeUnit.SECONDS.sleep(1);

        System.out.println("主线程在子线程变更后:" + threadlocal.get());
        threadlocal.get().setAge(99);
        System.out.println("主线程变更后:" + threadlocal.get());

        executorService.submit(() -> {
            System.out.println("子线程在主线程变更后:" + threadlocal.get());
        });

    }
}
