package com.feng.jdk.concurrency.shareresource;

/**
 * @author fengsy
 * @date 5/12/21
 * @Description
 */
public abstract class IntGenerator {
    private volatile boolean canceled = false;

    public abstract int next();

    public void cancel() {
        canceled = true;
    }

    public boolean isCanceled() {
        return canceled;
    }

}
