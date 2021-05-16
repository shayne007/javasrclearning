package com.feng.concurrency.threading;

/**
 * @author fengsy
 * @date 5/12/21
 * @Description
 */
public class MainThread {
    public static void main(String[] args) {
        LiftOff task = new LiftOff();
        task.run();
        // new Thread(task).start();
    }

}
