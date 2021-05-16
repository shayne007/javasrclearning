package com.feng.concurrency.simulation.carbuilder;

/**
 * @author fengsy
 * @date 5/14/21
 * @Description
 */
public class Car {
    private final int id;
    private boolean engine = false, driveTrain = false, wheels = false;

    public Car(int idn) {
        id = idn;
    }

    // Empty Car object:
    public Car() {
        id = -1;
    }

    public synchronized int getId() {
        return id;
    }

    public synchronized void addEngine() {
        engine = true;
    }

    public synchronized void addDriveTrain() {
        driveTrain = true;
    }

    public synchronized void addWheels() {
        wheels = true;
    }

    public synchronized String toString() {
        return "Car " + id + " [" + " engine: " + engine + " driveTrain: " + driveTrain + " wheels: " + wheels + " ]";
    }
}
