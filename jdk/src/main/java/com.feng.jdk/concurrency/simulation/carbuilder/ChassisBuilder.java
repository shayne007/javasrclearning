package com.feng.jdk.concurrency.simulation.carbuilder;

import java.util.concurrent.TimeUnit;

import static net.mindview.util.Print.print;

/**
 * @author fengsy
 * @date 5/14/21
 * @Description
 */
public class ChassisBuilder implements Runnable {
    private CarQueue carQueue;
    private int counter = 0;

    public ChassisBuilder(CarQueue cq) {
        carQueue = cq;
    }

    public void run() {
        try {
            while (!Thread.interrupted()) {
                TimeUnit.MILLISECONDS.sleep(500);
                // Make chassis:
                Car c = new Car(counter++);
                print("ChassisBuilder created " + c);
                // Insert into queue
                carQueue.put(c);
            }
        } catch (InterruptedException e) {
            print("Interrupted: ChassisBuilder");
        }
        print("ChassisBuilder off");
    }
}
