package com.feng.jdk.concurrency.deadlock.philosopher;

/**
 * @author fengsy
 * @date 5/13/21
 * @Description
 */
public class Chopstick {
    private boolean taken = false;

    public synchronized void take() throws InterruptedException {
        while (taken) {
            wait();
        }
        taken = true;
    }

    public synchronized void drop() {
        taken = false;
        notifyAll();
    }
}
