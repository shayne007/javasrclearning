package com.feng.jdk.concurrency.simulation.restaurant;

import java.util.concurrent.SynchronousQueue;

import static net.mindview.util.Print.print;

/**
 * @author fengsy
 * @date 5/14/21
 * @Description
 */
public class Customer implements Runnable {
    private static int counter = 0;
    private final int id = counter++;
    private final WaitPerson waitPerson;
    // Only one course at a time can be received:
    private SynchronousQueue<Plate> placeSetting = new SynchronousQueue<Plate>();

    public Customer(WaitPerson w) {
        waitPerson = w;
    }

    public void deliver(Plate p) throws InterruptedException {
        // Only blocks if customer is still
        // eating the previous course:
        placeSetting.put(p);
    }

    @Override
    public void run() {
        for (Course course : Course.values()) {
            Food food = course.randomSelection();
            try {
                waitPerson.placeOrder(this, food);
                // Blocks until course has been delivered:
                print(this + "eating " + placeSetting.take());
            } catch (InterruptedException e) {
                print(this + "waiting for " + course + " interrupted");
                break;
            }
        }
        print(this + "finished meal, leaving");
    }

    @Override
    public String toString() {
        return "Customer " + id + " ";
    }
}
