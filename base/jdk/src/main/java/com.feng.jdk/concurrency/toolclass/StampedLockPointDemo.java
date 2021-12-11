package com.feng.jdk.concurrency.toolclass;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.StampedLock;

/**
 * StampedLock使用示例：锁的降级和升级
 */
class StampedLockPointDemo {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);
        Point p = new Point();
        for (int i = 0; i < 1000; i++) {
            pool.submit(() -> {
//                p.moveIfAtOrigin(3, 4);
                double distance = p.distanceFromOrigin();
                if (distance != 5.0) {
                    System.out.println(distance);
                }
            });
        }
        p.move(3, 4);
        pool.shutdown();
        pool.awaitTermination(10, TimeUnit.SECONDS);
        System.out.println("done");

    }

    static class Point {
        private double x, y;
        private final StampedLock sl = new StampedLock();

        void move(double deltaX, double deltaY) { // an exclusively locked method
            long stamp = sl.writeLock();
            try {
                x += deltaX;
                y += deltaY;
                System.out.println("写锁开始赋值。。。。。");
            } finally {
                sl.unlockWrite(stamp);
            }
        }

        /**
         * 计算到原点的距离
         *
         * @return
         */
        double distanceFromOrigin() {
            // 乐观读
            long stamp = sl.tryOptimisticRead();

            // 读入局部变量，读的过程数据可能被修改
            double curX = x, curY = y;
            System.out.println(Thread.currentThread().getName() + ",乐观读：x: " + curX + ",y: " + curY);
            // 判断执行读操作期间，是否存在写操作，如果存在，则sl.validate返回false
            if (!sl.validate(stamp)) {
                // 升级为悲观读锁
                stamp = sl.readLock();
                try {
                    curX = x;
                    curY = y;
                    System.out.println(Thread.currentThread().getName() + ",悲观读锁：x: " + curX + ",y: " + curY);
                } finally {
                    // 释放悲观读锁
                    sl.unlockRead(stamp);
                }
            }
            return Math.sqrt(curX * curX + curY * curY);
        }

        void moveIfAtOrigin(double newX, double newY) { // upgrade
            // Could instead start with optimistic, not read mode
            long stamp = sl.readLock();
            try {
                while (x == 0.0 && y == 0.0) {
                    long ws = sl.tryConvertToWriteLock(stamp);
                    if (ws != 0L) {
                        stamp = ws;//读锁升级为写锁后使用新的stamp
                        x = newX;
                        y = newY;
                        break;
                    } else {
                        sl.unlockRead(stamp);
                        stamp = sl.writeLock();
                    }
                }
            } finally {
                sl.unlock(stamp);
            }
        }
    }
}