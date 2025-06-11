package com.feng.jdk.concurrency.patterns.masterslave.reusable;

import com.feng.jdk.concurrency.patterns.twophaseterminate.reusable.AbstractTerminatableThread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * @author fengsy
 * @date 5/19/21
 * @Description
 */
public abstract class WorkerThreadSlave<T, V> extends AbstractTerminatableThread implements SlaveSpec<T, V> {

    private final BlockingQueue<Runnable> taskQueue;

    protected WorkerThreadSlave(BlockingQueue<Runnable> taskQueue) {
        this.taskQueue = taskQueue;
    }

    @Override
    public Future<V> submit(final T task) throws InterruptedException {
        FutureTask<V> ft = new FutureTask<>(new Callable<V>() {
            @Override
            public V call() throws Exception {
                V result;

                try {
                    result = doProcess(task);
                } catch (Exception e) {
                    SubTaskFailureException stfe = newSubTaskFailureException(task, e);
                    throw stfe;
                }

                return result;
            }
        });
        taskQueue.put(ft);
        terminationToken.reservations.incrementAndGet();
        return ft;
    }

    private SubTaskFailureException newSubTaskFailureException(final T subTask, Exception cause) {
        RetryInfo<T, V> retryInfo = new RetryInfo<T, V>(subTask, new Callable<V>() {
            @Override
            public V call() throws Exception {
                V result;
                result = doProcess(subTask);
                return result;
            }

        });

        return new SubTaskFailureException(retryInfo, cause);
    }

    /**
     * 留给子类实现。用于实现子任务的处理逻辑。
     *
     * @param task 子任务
     * @return 子任务的处理结果
     * @throws Exception
     */
    protected abstract V doProcess(T task) throws Exception;

    @Override
    protected void doRun() throws Exception {
        try {
            Runnable task = taskQueue.take();
            task.run();
        } finally {
            terminationToken.reservations.decrementAndGet();
        }

    }

    @Override
    public void init() {
        start();
    }

    @Override
    public void shutdown() {
        terminate(true);
    }
}
