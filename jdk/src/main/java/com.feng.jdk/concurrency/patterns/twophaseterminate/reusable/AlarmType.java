package com.feng.jdk.concurrency.patterns.twophaseterminate.reusable;

/**
 * @author fengsy
 * @date 5/18/21
 * @Description
 */
public enum AlarmType {
    FAULT("fault"), RESUME("resume");

    private final String name;

    private AlarmType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {

        return name;
    }
}
