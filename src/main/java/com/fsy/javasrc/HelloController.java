package com.fsy.javasrc;

import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import lombok.extern.slf4j.Slf4j;

/**
 * @author fengsy
 * @date 1/6/21
 * @Description
 */

@RestController
@Slf4j
public class HelloController {

    @RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }

    @RequestMapping(value = "/test0")
    public String test0(HttpServletRequest request) {
        ThreadLocal<Byte[]> localVariable = new ThreadLocal<Byte[]>();
        try {
            localVariable.set(new Byte[4096 * 1024]);// 为线程添加变量

            //

        } finally {
            localVariable.remove();
        }

        return "success";
    }

    private static ThreadPoolExecutor threadPool =
        new ThreadPoolExecutor(2, 2, 1, TimeUnit.HOURS, new ArrayBlockingQueue<>(100),
            new ThreadFactoryBuilder().setNameFormat("batchfileprocess-threadpool-%d").build(),
            new ThreadPoolExecutor.CallerRunsPolicy());
    private static ThreadPoolExecutor asyncCalcThreadPool = new ThreadPoolExecutor(200, 200, 1, TimeUnit.HOURS,
        new ArrayBlockingQueue<>(1000), new ThreadFactoryBuilder().setNameFormat("asynccalc-threadpool-%d").build());

    @GetMapping("right2")
    public int right2() throws ExecutionException, InterruptedException {
        return asyncCalcThreadPool.submit(calcTask()).get();
    }

    private Callable<Integer> calcTask() {
        return () -> {
            TimeUnit.MILLISECONDS.sleep(10);
            return 1;
        };
    }

    @GetMapping("wrong")
    public int wrong() throws ExecutionException, InterruptedException {
        return threadPool.submit(calcTask()).get();
    }

    @GetMapping("right")
    public int right() throws InterruptedException {
        // 使用一个计数器跟踪完成的任务数
        AtomicInteger atomicInteger = new AtomicInteger();
        // 创建一个具有2个核心线程、5个最大线程，使用容量为10的ArrayBlockingQueue阻塞队列作为工作队列的线程池，使用默认的AbortPolicy拒绝策略
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(2, 5, 5, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10),
            new ThreadFactoryBuilder().setNameFormat("demo-threadpool-%d").build(),
            new ThreadPoolExecutor.AbortPolicy());

        // threadPool.prestartAllCoreThreads();
        // threadPool.allowCoreThreadTimeOut(true);
        printStats(threadPool);
        // 每隔1秒提交一次，一共提交20次任务
        IntStream.rangeClosed(1, 20).forEach(i -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int id = atomicInteger.incrementAndGet();
            try {
                threadPool.submit(() -> {
                    log.info("{} started", id);
                    // 每个任务耗时10秒
                    try {
                        TimeUnit.SECONDS.sleep(10);
                    } catch (InterruptedException e) {
                    }
                    log.info("{} finished", id);
                });
            } catch (Exception ex) {
                // 提交出现异常的话，打印出错信息并为计数器减一
                log.error("error submitting task {}", id, ex);
                atomicInteger.decrementAndGet();
            }
        });

        TimeUnit.SECONDS.sleep(60);
        return atomicInteger.intValue();
    }

    @GetMapping("oom1")
    public void oom1() throws InterruptedException {

        ThreadPoolExecutor threadPool = (ThreadPoolExecutor)Executors.newFixedThreadPool(1);
        // 打印线程池的信息，稍后我会解释这段代码
        printStats(threadPool);
        for (int i = 0; i < 100000000; i++) {
            threadPool.execute(() -> {
                String payload = IntStream.rangeClosed(1, 1000000).mapToObj(__ -> "a").collect(Collectors.joining(""))
                    + UUID.randomUUID().toString();
                try {
                    TimeUnit.HOURS.sleep(1);
                } catch (InterruptedException e) {
                }
                log.info(payload);
            });
        }

        threadPool.shutdown();
        threadPool.awaitTermination(1, TimeUnit.HOURS);
    }

    private void printStats(ThreadPoolExecutor threadPool) {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            log.info("=========================");
            log.info("Pool Size: {}", threadPool.getPoolSize());
            log.info("Active Threads: {}", threadPool.getActiveCount());
            log.info("Number of Tasks Completed: {}", threadPool.getCompletedTaskCount());
            log.info("Number of Tasks in Queue: {}", threadPool.getQueue().size());

            log.info("=========================");
        }, 0, 1, TimeUnit.SECONDS);
    }

}