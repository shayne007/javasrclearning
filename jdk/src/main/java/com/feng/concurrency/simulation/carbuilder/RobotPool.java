package com.feng.concurrency.simulation.carbuilder;

import java.util.HashSet;
import java.util.Set;

/**
 * @author fengsy
 * @date 5/14/21
 * @Description
 */
public class RobotPool {
    // Quietly prevents identical entries:
    private Set<Robot> pool = new HashSet<Robot>();

    public synchronized void add(Robot r) {
        pool.add(r);
        notifyAll();
    }

    public synchronized void hire(Class<? extends Robot> robotType, Assembler d) throws InterruptedException {
        for (Robot r : pool)
            if (r.getClass().equals(robotType)) {
                pool.remove(r);
                r.assignAssembler(d);
                r.engage(); // Power it up to do the task
                return;
            }
        wait(); // None available
        hire(robotType, d); // Try again, recursively
    }

    public synchronized void release(Robot r) {
        add(r);
    }

}
