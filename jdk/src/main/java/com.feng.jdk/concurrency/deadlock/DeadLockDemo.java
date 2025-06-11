package com.feng.jdk.concurrency.deadlock;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author fengsy
 * @date 6/9/21
 * @Description
 */

public class DeadLockDemo {

    public static void main(String[] args) throws InterruptedException {

        ThreadMXBean mbean = ManagementFactory.getThreadMXBean();
        Runnable dlCheck = () -> {
            long[] threadIds = mbean.findDeadlockedThreads();
            if (threadIds != null) {
                ThreadInfo[] threadInfos = mbean.getThreadInfo(threadIds);
                System.out.println("Detected deadlock threads:");
                for (ThreadInfo threadInfo : threadInfos) {
                    System.out.println(threadInfo.getThreadName());
                }
            }
        };

        ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(1);
        // 稍等5秒，然后每10秒进行一次死锁扫描
        scheduler.scheduleAtFixedRate(dlCheck, 5L, 10L, TimeUnit.SECONDS);
        // 死锁样例代码…
        String lockA = "lockA";
        String lockB = "lockB";
        Thread t1 = new TaskThread("Thread1", lockA, lockB);
        Thread t2 = new TaskThread("Thread2", lockB, lockA);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }

    static class TaskThread extends Thread {
        private String first;
        private String second;

        public TaskThread(String name, String first, String second) {
            super(name);
            this.first = first;
            this.second = second;
        }

        @Override
        public void run() {
            synchronized (first) {
                System.out.println(this.getName() + " obtained: " + first);
                try {
                    Thread.sleep(1000L);
                    synchronized (second) {
                        System.out.println(this.getName() + " obtained: " + second);
                    }
                } catch (InterruptedException e) {
                    // Do nothing
                }
            }
        }
    }


}