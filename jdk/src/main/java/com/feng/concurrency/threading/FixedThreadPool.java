package com.feng.concurrency.threading;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <p>
 * A thread pool should be created by ThreadPoolExecutor rather than Executors. These would make the parameters of the
 * thread pool understandable. It would also reduce the risk of running out of system resource.
 * </p>
 * SingleThreadPool or FixedThreadPool:
 * <p>
 * Maximum request queue size Integer.MAX_VALUE. A large number of requests might cause OOM
 * </p>
 * 
 * @author fengsy
 * @date 5/12/21
 * @Description
 */
public class FixedThreadPool {
    public static void main(String[] args) {
        ExecutorService exec = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 5; i++) {
            exec.execute(new LiftOff());
        }
        exec.shutdown();
    }
}
