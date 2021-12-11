package com.feng.jdk.concurrency.toolclass;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 使用CompletionService 解决批量的并行任务的协作问题
 *
 * @author fengsy
 * @date 1/19/21
 * @Description
 */
public class CompletionServiceDemo {
    public static void main(String[] args) {
        System.out.println(getMin());
        System.out.println(getOne());
    }

    /**
     * 多个接口提供相同的服务，其中之一调用成功则返回结果
     * 使用completionService使得先执行完的调用先处理；
     * 将CompletionService返回的Future对象保存到List中用于后续的cancel处理，及时释放资源
     *
     * @return
     */
    static Integer getOne() {
        ExecutorService executor = Executors.newFixedThreadPool(3);
        // 创建CompletionService
        CompletionService<Integer> completionService = new ExecutorCompletionService<>(executor);
        // 用于保存Future对象
        List<Future<Integer>> futures = new ArrayList<>(3);

        // 异步向电商S1询价
        futures.add(completionService.submit(() -> getPriceByS1()));
        // 异步向电商S2询价
        futures.add(completionService.submit(() -> getPriceByS2()));
        // 异步向电商S3询价
        futures.add(completionService.submit(() -> getPriceByS3()));

        // 获取最快返回的任务执行结果
        Integer result = 0;
        try {
            // 只要有一个成功返回，则break
            for (int i = 0; i < 3; ++i) {
                result = completionService.take().get();
                //简单地通过判空来检查是否成功返回
                if (result != null) {
                    break;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } finally {
            //取消所有任务
            for (Future<Integer> f : futures)
                f.cancel(true);
        }
        // 返回结果
        return result;


    }

    /**
     * 多个接口提供相同的服务，调用结果存数据库，选出其中返回值最小的结果
     * 使用completionService使得先执行完的调用先处理；
     * 存数据库操作使用线程池异步处理；
     * 计算最小值时需等待所有接口都调用完成，线程等待使用CountDownLatch；
     * AtomicReference<Integer>保存最小值，计算最小值不是原子操作，使用synchronized解决并发问题
     *
     * @return
     */
    static AtomicReference<Integer> getMin() {
        // 创建线程池
        ExecutorService executor = Executors.newFixedThreadPool(3);
        // 创建CompletionService
        CompletionService<Integer> completionService = new ExecutorCompletionService<>(executor);
        // 异步向电商S1询价
        completionService.submit(() -> getPriceByS1());
        // 异步向电商S2询价
        completionService.submit(() -> getPriceByS2());
        // 异步向电商S3询价
        completionService.submit(() -> getPriceByS3());
        // 将询价结果异步保存到数据库
        // 并计算最低报价
        AtomicReference<Integer> min = new AtomicReference<>(Integer.MAX_VALUE);
        AtomicInteger max = new AtomicInteger(Integer.MIN_VALUE);
        CountDownLatch latch = new CountDownLatch(3);
        for (int i = 0; i < 3; i++) {
            executor.execute(() -> {
                Integer result = null;
                try {
                    result = completionService.take().get();
                } catch (Exception e) {
                }
                //模拟数据库保存数据
                save(result);
                //计算最低报价
//                synchronized (min) {
//                    min.set(Integer.min(min.get(), result));
//                }
                final Integer finalResult = result;
                min.getAndUpdate(v -> Integer.min(v, finalResult));
                max.getAndUpdate(v -> Integer.max(v, finalResult));
                latch.countDown();
            });
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("max: " + max);
        return min;
    }


    private static void save(Integer r) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static Integer getPriceByS3() throws InterruptedException {
        long workTime = (long) Math.random() * 5;
        TimeUnit.SECONDS.sleep(workTime);
        return 3;
    }

    private static Integer getPriceByS2() throws InterruptedException {
        TimeUnit.SECONDS.sleep((long) Math.random() * 5);
        return 2;
    }

    private static Integer getPriceByS1() throws InterruptedException {
        TimeUnit.SECONDS.sleep((long) Math.random() * 5);
        return 1;
    }
}
