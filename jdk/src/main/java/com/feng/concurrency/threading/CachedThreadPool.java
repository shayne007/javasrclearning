package com.feng.concurrency.threading;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * A thread pool should be created by ThreadPoolExecutor rather than Executors. These would make the parameters of the
 * thread pool understandable. It would also reduce the risk of running out of system resource.
 * </p>
 * CachedThreadPool:
 * <p>
 * The number of threads which are allowed to be created is Integer.MAX_VALUE. Creating too many threads might lead to
 * OOM
 * </p>
 * 
 * 
 * @author fengsy
 * @date 5/12/21
 * @Description
 */
public class CachedThreadPool {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < 5; i++) {
            executorService.execute(new LiftOff());
        }
        // executorService.shutdown();
        executorService.awaitTermination(1000, TimeUnit.MILLISECONDS);
        System.out.println("shutdown...");
    }

}
