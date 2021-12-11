package com.feng.jdk.concurrency.simulation.bankteller;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author fengsy
 * @date 5/14/21
 * @Description
 */
public class CustomerLine extends ArrayBlockingQueue<Customer> {

    public CustomerLine(int maxLineSize) {
        super(maxLineSize);
    }

    @Override
    public String toString() {
        if (this.size() == 0) {
            return "[Empty]";
        }
        StringBuilder result = new StringBuilder();
        for (Customer customer : this) {
            result.append(customer);
        }
        return result.toString();
    }
}
