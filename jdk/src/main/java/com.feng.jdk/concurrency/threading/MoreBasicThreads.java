package com.feng.jdk.concurrency.threading;

/**
 * @author fengsy
 * @date 5/12/21
 * @Description
 */
public class MoreBasicThreads {
    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            new Thread(new LiftOff()).start();
        }
        System.out.println("Waiting for LiftOff");
    }
}
