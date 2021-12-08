package com.feng.concurrency.patterns.guardedsuspension.reusable;

import java.util.concurrent.Callable;

/**
 * @author fengsy
 * @date 5/17/21
 * @Description
 */
public abstract class GuardedAction<V> implements Callable<V> {
    protected final Predicate guard;

    public GuardedAction(Predicate guard) {
        this.guard = guard;
    }
}
