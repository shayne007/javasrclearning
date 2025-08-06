package com.feng.jdk.concurrency.patterns.tspecstorage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.feng.jdk.tspecstorage.Stu;

/**
 * @author fengsy
 * @date 5/18/21
 * @Description
 */
public class TransmittableThreadLocalTest {

    public static final ThreadLocal<Stu> threadlocal = new MyTransmittableThreadLocal<>();

    public static ExecutorService executorService = Executors.newFixedThreadPool(1);

    public static void main(String[] args) throws InterruptedException {
        threadlocal.set(new Stu("zhangsan", 11));
        TimeUnit.SECONDS.sleep(1);

        executorService.submit(() -> {
            System.out.println("子线程:" + threadlocal.get());
        });
        TimeUnit.SECONDS.sleep(1);

        threadlocal.get().setAge(22);
        TimeUnit.SECONDS.sleep(1);

        executorService.submit(() -> {
            System.out.println("5子线程变更前:" + threadlocal.get());
            threadlocal.get().setAge(33);
            System.out.println("6子线程变更后:" + threadlocal.get());
        });
        TimeUnit.SECONDS.sleep(1);
        threadlocal.get().setAge(44);
        TimeUnit.SECONDS.sleep(1);

        executorService.submit(() -> {
            System.out.println("7子线程:" + threadlocal.get());
        });
        TimeUnit.SECONDS.sleep(1);
        System.out.println(threadlocal.get());
        executorService.shutdown();
    }
}
