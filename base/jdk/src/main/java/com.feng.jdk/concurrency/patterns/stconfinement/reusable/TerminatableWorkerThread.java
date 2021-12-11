package com.feng.jdk.concurrency.patterns.stconfinement.reusable;

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
public class TerminatableWorkerThread<T, V> extends AbstractTerminatableThread {
    private final BlockingQueue<Runnable> workQueue;
    // 负责真正执行任务的对象
    private final TaskProcessor<T, V> taskProcessor;

    public TerminatableWorkerThread(BlockingQueue workQueue, TaskProcessor<T, V> taskProcessor) {
        this.workQueue = workQueue;
        this.taskProcessor = taskProcessor;
    }

    /**
     * 接收并行任务，并将其串行化。
     *
     * @param task 任务
     * @return 可借以获取任务处理结果的Promise（参见第6章，Promise模式）实例。
     * @throws InterruptedException
     */
    public Future<V> submit(final T task) throws InterruptedException {
        Callable<V> callable = new Callable<V>() {

            @Override
            public V call() throws Exception {
                return taskProcessor.doProcess(task);
            }

        };

        FutureTask<V> ft = new FutureTask<V>(callable);
        workQueue.put(ft);

        terminationToken.reservations.incrementAndGet();
        return ft;
    }

    /**
     * 执行任务的处理逻辑
     */
    @Override
    protected void doRun() throws Exception {
        Runnable ft = workQueue.take();
        try {
            ft.run();
        } finally {
            terminationToken.reservations.decrementAndGet();
        }
    }
}
