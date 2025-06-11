package com.feng.jdk.concurrency.patterns.masterslave.reusable;

import java.util.concurrent.Callable;

/**
 * @author fengsy
 * @date 5/19/21
 * @Description
 */
public class RetryInfo<T, V> {
    public final T subTask;
    public final Callable<V> redoCommand;

    public RetryInfo(T subTask, Callable<V> redoCommand) {
        this.subTask = subTask;
        this.redoCommand = redoCommand;
    }
}
