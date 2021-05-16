package com.feng.concurrency.threading;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author fengsy
 * @date 5/12/21
 * @Description
 */
public class SimplePriorities implements Runnable {
    private int countDown = 5;
    // No optimization
    private int priority;
    private volatile double d;

    @Override
    public String toString() {
        return Thread.currentThread() + ": " + countDown;
    }

    public SimplePriorities(int priority) {
        this.priority = priority;
    }

    /**
     * 执行过程中每个线程打印5次的线程信息
     */
    @Override
    public void run() {
        Thread.currentThread().setPriority(priority);
        while (true) {
            // An expensive, interruptable operation:
            for (int i = 1; i < 100000; i++) {
                d += (Math.PI + Math.E) / (double)i;
                if (i % 1000 == 0) {
                    Thread.yield();
                }
            }
            System.out.println(this);
            if (--countDown == 0) {
                return;
            }
        }
    }

    /**
     * 启动5个优先级最低的任务，1优先级最高的任务
     * 
     * 根据打印信息，分析每次线程的执行先后顺序
     * 
     * 结果显示：高优先级不能保证先执行
     * 
     * @param args
     */
    public static void main(String[] args) {
        ExecutorService exec = Executors.newCachedThreadPool();
        for (int i = 0; i < 5; i++) {
            exec.execute(new SimplePriorities(Thread.MIN_PRIORITY));
        }
        exec.execute(new SimplePriorities(Thread.MAX_PRIORITY));
        exec.shutdown();
    }
}
