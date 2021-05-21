package com.feng.concurrency.patterns.poolworker;

import com.feng.concurrency.util.Debug;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author fengsy
 * @date 5/20/21
 * @Description
 */
public class ThreadPoolDeadLockAvoidance {
    private final ThreadPoolExecutor threadPool = new ThreadPoolExecutor(1, 1, 60, TimeUnit.SECONDS,
        new SynchronousQueue<Runnable>(), new ThreadPoolExecutor.CallerRunsPolicy());

    public static void main(String[] args) throws InterruptedException {
        ThreadPoolDeadLockAvoidance me = new ThreadPoolDeadLockAvoidance();
        me.test("this will not deadlock");
    }

    private void test(final String msg) throws InterruptedException {
        Runnable taskA = new Runnable() {
            @Override
            public void run() {
                Debug.info("Executing taskA");
                Runnable taskB = new Runnable() {
                    @Override
                    public void run() {
                        Debug.info("Executing taskB: " + msg);

                    }
                };
                Future<?> result = threadPool.submit(taskB);
                try {
                    result.get();
                } catch (InterruptedException e) {
                    ;
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        };
        threadPool.submit(taskA);
    }
}
