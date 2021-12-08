package com.feng.concurrency.simulation.carbuilder;

import static net.mindview.util.Print.print;

/**
 * @author fengsy
 * @date 5/14/21
 * @Description
 */
public class Reporter implements Runnable {
    private CarQueue carQueue;

    public Reporter(CarQueue cq) {
        carQueue = cq;
    }

    public void run() {
        try {
            while (!Thread.interrupted()) {
                print(carQueue.take());
            }
        } catch (InterruptedException e) {
            print("Exiting Reporter via interrupt");
        }
        print("Reporter off");
    }
}
