package com.feng.jdk.concurrency.patterns.immutableobject;

/**
 * @author fengsy
 * @date 5/18/21
 * @Description
 */
public final class ImmutableLocation {
    private final double x;
    private final double y;

    public ImmutableLocation(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
