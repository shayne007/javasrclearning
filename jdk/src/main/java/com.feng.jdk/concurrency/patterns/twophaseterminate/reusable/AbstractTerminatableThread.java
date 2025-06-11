package com.feng.jdk.concurrency.patterns.twophaseterminate.reusable;

import org.apache.log4j.Logger;

/**
 * @author fengsy
 * @date 5/18/21
 * @Description
 */
public abstract class AbstractTerminatableThread extends Thread implements Terminatable {
    final static Logger logger = Logger.getLogger(AbstractTerminatableThread.class);
    private final boolean DEBUG = true;
    public final TerminationToken terminationToken;

    public AbstractTerminatableThread() {
        this(new TerminationToken());
    }

    /**
     * @param terminationToken 线程间共享的线程终止标志实例
     */
    public AbstractTerminatableThread(TerminationToken terminationToken) {
        this.terminationToken = terminationToken;
        terminationToken.register(this);
    }

    /**
     * 留给子类实现其线程处理逻辑。
     *
     * @throws Exception
     */
    protected abstract void doRun() throws Exception;

    /**
     * 留给子类实现。用于实现线程停止后的一些清理动作。
     *
     * @param cause
     */
    protected void doCleanup(Exception cause) {
        // 什么也不做
    }

    /**
     * 留给子类实现。用于执行线程停止所需的操作。 子类在该方法中实现一些关闭目标线程所需的额外操作。
     * <p>
     * 比如：关闭socket同步io
     */
    protected void doTerminiate() {
        // 什么也不做
    }

    @Override
    public void run() {
        Exception ex = null;
        try {
            for (; ; ) {
                // 在执行线程的处理逻辑前先判断线程停止的标志。
                if (terminationToken.isToShutdown() && terminationToken.reservations.get() <= 0) {
                    break;
                }
                doRun();
            }
        } catch (Exception e) {
            ex = e;
            if (e instanceof InterruptedException) {
                if (DEBUG) {
                    logger.debug(e);
                }
            } else {
                logger.error("other unexpected exception", e);
            }
        } finally {
            try {
                doCleanup(ex);
            } finally {
                terminationToken.notifyThreadTermination(this);
            }
        }
    }

    @Override
    public void interrupt() {
        terminate();
    }

    @Override
    public void terminate() {
        terminationToken.setToShutdown(true);
        try {
            doTerminiate();
        } finally {

            // 若无待处理的任务，则试图强制终止线程
            if (terminationToken.reservations.get() <= 0) {
                super.interrupt();
            }
        }
    }

    public void terminate(boolean waitUtilThreadTerminated) {
        terminate();
        if (waitUtilThreadTerminated) {
            try {
                this.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
