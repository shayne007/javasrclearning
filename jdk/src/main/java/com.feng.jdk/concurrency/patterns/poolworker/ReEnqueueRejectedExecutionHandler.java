package com.feng.jdk.concurrency.patterns.poolworker;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author fengsy
 * @date 5/19/21
 * @Description
 */
public class ReEnqueueRejectedExecutionHandler implements RejectedExecutionHandler {

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        if (executor.isShutdown()) {
            return;
        }
        try {
            executor.getQueue().put(r);
        } catch (InterruptedException e) {
            ;
        }
    }

}