package com.feng.concurrency.simulation.bankteller;

/**
 * @author fengsy
 * @date 5/14/21
 * @Description
 */
public class Customer {
    private final int serviceTime;

    public Customer(int tm) {
        serviceTime = tm;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    @Override
    public String toString() {
        return "[" + serviceTime + "]";
    }
}
