package com.feng.jdk.concurrency.patterns.twophaseterminate.reusable;

/**
 * @author fengsy
 * @date 5/18/21
 * @Description
 */
public class DelegatingTerminatableThread extends AbstractTerminatableThread {
    private final Runnable task;

    public DelegatingTerminatableThread(Runnable task) {
        this.task = task;
    }

    @Override
    protected void doRun() throws Exception {
        this.task.run();
    }

    public static AbstractTerminatableThread of(Runnable task) {
        DelegatingTerminatableThread ret = new DelegatingTerminatableThread(task);
        return ret;
    }
}
