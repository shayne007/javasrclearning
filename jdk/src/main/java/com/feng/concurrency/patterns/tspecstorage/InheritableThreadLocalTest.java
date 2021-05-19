package com.feng.concurrency.patterns.tspecstorage;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author fengsy
 * @date 5/18/21
 * @Description
 */
public class InheritableThreadLocalTest {

    public static final ThreadLocal<Stu> threadlocal = new MyInheritableThreadLocal<>();

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
