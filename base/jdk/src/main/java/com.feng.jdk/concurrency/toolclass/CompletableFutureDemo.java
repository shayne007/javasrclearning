package com.feng.jdk.concurrency.toolclass;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author fengsy
 * @date 1/19/21
 * @Description
 */
class CompletableFutureDemo {
    public static void main(String[] args) {
//        completableFutureTestSerial();
        completableFutureTestOr();
//        completableFutureTestAnd();
//        exceptionHandler();
    }


    /**
     * 串行关系：1.前序任务没有返回值；2.后序任务需要用到前序任务的返回值
     * 实际生产场景使用简单的同步处理即可
     */
    private static void completableFutureTestSerial() {
        //1.1 提交无返回值的任务至共享线程池
        CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> {
            System.out.println("execute t1.1 ...");
        });
        //1.2 使用thenRun添加串行任务，在1.1执行完成后执行
        future1.thenRun(() -> System.out.println("execute t1.2 ..."));

        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            System.out.println("execute t2.1 ...");
            return "t2.1 Done";
        });
        future2.thenAccept(str -> System.out.println(str + ", execute t2.2 ..."));

        //使用thenApply对CompletableFuture的返回值进行串行化的多次函数处理
        CompletableFuture<String> f0 =
                CompletableFuture.supplyAsync(() -> "Hello World")      //①
                        .thenApply(s -> s + " QQ")  //②
                        .thenApply(String::toUpperCase);//③

        System.out.println(f0.join());
    }

    /**
     * OR 聚合关系: applyToEither
     */
    private static void completableFutureTestOr() {

        CompletableFuture<String> f1 =
                CompletableFuture.supplyAsync(() -> {
                    int t = getRandom(5, 10);
                    System.out.println(t);
                    sleep(t, TimeUnit.SECONDS);
                    return "f1:" + String.valueOf(t);
                });

        CompletableFuture<String> f2 =
                CompletableFuture.supplyAsync(() -> {
                    int t = getRandom(5, 10);
                    System.out.println(t);
                    sleep(t, TimeUnit.SECONDS);
                    return "f2:" + String.valueOf(t);
                });

        //两个
        CompletableFuture<String> f3 = f1.applyToEither(f2, s -> {
            System.out.println(s);
            return "f3:" + s;
        });
        //两个以上
        CompletableFuture<String> f4 = CompletableFuture.anyOf(f1, f2, f3).thenApply(s -> {
                    System.out.println(s);
                    return "f4:" + s;
                }
        );
        System.out.println("main:" + f3.join());
        System.out.println("main:" + f4.join());
    }

    private static int getRandom(int i, int i1) {
        return new Random().nextInt(5) + 5;
    }

    /**
     * AND 聚合关系: thenCombine
     */
    private static void completableFutureTestAnd() {
        // 任务1：洗水壶->烧开水
        CompletableFuture<String> f1 = CompletableFuture.supplyAsync(() -> {

            System.out.println(Thread.currentThread().getName() + " T1:洗水壶...");
            sleep(1, TimeUnit.SECONDS);

            System.out.println(Thread.currentThread().getName() + " T1:烧开水...");
            sleep(5, TimeUnit.SECONDS);
            return "泉水";
        });
        // 任务2：洗茶壶->洗茶杯->拿茶叶
        CompletableFuture<String> f2 = CompletableFuture.supplyAsync(() -> {

            System.out.println(Thread.currentThread().getName() + " T2:洗茶壶...");
            sleep(1, TimeUnit.SECONDS);

            System.out.println(Thread.currentThread().getName() + " T2:洗茶杯...");
            sleep(2, TimeUnit.SECONDS);

            System.out.println(Thread.currentThread().getName() + " T2:拿茶叶...");
            sleep(1, TimeUnit.SECONDS);
            return "龙井";
        });
        // 任务3：任务1和任务2完成后执行：泡茶
        CompletableFuture<String> f3 = f1.thenCombine(f2, (__, tf2) -> {

            System.out.println(Thread.currentThread().getName() + " T3:从T1拿到开水:" + __);
            System.out.println(Thread.currentThread().getName() + " T3:从T2拿到茶叶:" + tf2);
            System.out.println(Thread.currentThread().getName() + " T3:泡茶...");
            return "上茶:" + __ + tf2;
        });
        // 等待任务3执行结果

        System.out.println(Thread.currentThread().getName() + f3.join());

    }

    /**
     * 异常处理
     */
    private static void exceptionHandler() {

        CompletableFuture<Integer>
                f0 = CompletableFuture.supplyAsync(() -> (7 / 0))
                .thenApply(r -> r * 10)
                .exceptionally(e -> 0);
        System.out.println(f0.join());
    }

    private static void sleep(int t, TimeUnit u) {
        try {
            u.sleep(t);
        } catch (InterruptedException e) {
        }
    }

}