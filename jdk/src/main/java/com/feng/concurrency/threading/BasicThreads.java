package com.feng.concurrency.threading;

/**
 * @author fengsy
 * @date 5/12/21
 * @Description
 */
public class BasicThreads {
    public static void main(String[] args) {
        Thread thread = new Thread(new LiftOff());
        thread.start();
        System.out.println("Waiting for LiftOff!");
    }
}
