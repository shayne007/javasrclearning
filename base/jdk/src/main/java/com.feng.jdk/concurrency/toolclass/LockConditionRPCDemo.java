package com.feng.jdk.concurrency.toolclass;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description RPC调用 异步转同步
 * @Author fengsy
 * @Date 11/13/21
 */
public class LockConditionRPCDemo {

    Response response;
    // 创建锁与条件变量
    private final Lock lock = new ReentrantLock();
    private final Condition done = lock.newCondition();

    // 调用方通过该方法等待结果
    Object get(int timeout) throws TimeoutException, InterruptedException {
        long start = System.nanoTime();
        lock.lock();
        try {
            while (!isDone()) {
                done.await(timeout, TimeUnit.SECONDS);
                long cur = System.nanoTime();
                if (isDone() || cur - start > timeout) {
                    break;
                }
            }
        } finally {
            lock.unlock();
        }
        if (!isDone()) {
            throw new TimeoutException();
        }
        return response.getObject();
    }

    // RPC结果是否已经返回
    boolean isDone() {
        return response != null;
    }

    // RPC结果返回时调用该方法
    private void doReceived(Response res) {
        lock.lock();
        try {
            response = res;
            if (done != null) {
                done.signal();
            }
        } finally {
            lock.unlock();
        }
    }

    private class Response {
        Object object;

        public Object getObject() {
            return object;
        }
    }
}
