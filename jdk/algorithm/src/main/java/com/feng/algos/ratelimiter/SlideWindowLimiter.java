package com.feng.algos.ratelimiter;

import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Description 限流算法示例-使用循环链表实现滑动窗口，比如，窗口大小为1分钟，10个时间单元，每个时间单元6秒
 * @Author fengsy
 * @Date 9/22/21
 */
public class SlideWindowLimiter {
    private long window;
    private int slot;
    private long limit;
    private long slotTime;

    private Node lastNode;

    public SlideWindowLimiter(long window, int slot, long limit) {
        this.window = window;
        this.slot = slot;
        this.limit = limit;
        init();

    }

    /**
     * 初始化：构建循环链表
     */
    private void init() {
        Node currentNode = null;
        long currentTime = System.currentTimeMillis();
        for (int i = 0; i < slot; i++) {
            if (lastNode == null) {
                lastNode = new Node(i + 1, currentTime, null);
                currentNode = lastNode;
            } else {
                lastNode.next = new Node(i + 1, currentTime, null);
                lastNode = lastNode.next;
            }
        }
        lastNode.next = currentNode;
        slotTime = window / slot;
    }

    public synchronized boolean isLimited() {
        boolean flag = false;
        slide();
        long sum = getSum();

        System.out.println("sum: " + sum);
        printNodeInfo();
        if (sum > limit) {
            flag = true;
        }
        lastNode.addCount();

        return flag;
    }

    private void printNodeInfo() {
        Node currentNode = lastNode;
        for (int i = 0; i < slot; i++) {
            System.out.print(" id: " + currentNode.id);
            System.out.print(", count: " + currentNode.count);
            System.out.print(", time: " + currentNode.time);
            currentNode = currentNode.next;
        }
        System.out.println();
    }

    /**
     * 计算流逝的时间，经过了多少个slot
     */
    private void slide() {
        long now = System.currentTimeMillis();
        long time = lastNode.getTime();
        int num = (int)((now - time) / slotTime);
        if (num > slot) {
            num = slot;
        }
        reset(num, time);
    }

    /**
     * 重置流逝的时间内的滑动窗口数据
     *
     * @param num
     * @param time
     */
    private void reset(int num, long time) {
        if (num <= 0)
            return;
        Node currentNode = lastNode;
        for (int i = 0; i < num; i++) {
            currentNode = currentNode.next;
            currentNode.setCount(0L);
        }
        currentNode.setTime(time + num * slotTime);

        lastNode = currentNode;
    }

    private long getSum() {
        long sum = 0;
        for (int i = 0; i < slot; i++) {
            sum += lastNode.count;
            lastNode = lastNode.next;

        }
        return sum;
    }

    private class Node {
        private int id;
        private long time;
        private long count;
        private Node next;

        public Node(int id, long time, Node next) {
            this.id = id;
            this.time = time;
            this.next = next;
            this.count = 0;
        }

        public int getId() {
            return id;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public long getCount() {
            return count;
        }

        public void setCount(long count) {
            this.count = count;
        }

        public Node getNext() {
            return next;
        }

        public void addCount() {
            this.count = this.count + 1;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        SlideWindowLimiter limiter = new SlideWindowLimiter(3 * 1000, 10, 10);
        Thread.sleep(1000);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(4, 4, 0, TimeUnit.SECONDS, new LinkedBlockingQueue<>(10));
        for (int i = 0; i < 100; i++) {
            Thread.sleep(new Random().nextInt(500));
            executor.execute(() -> System.out.println(limiter.isLimited()));
        }
    }
}
