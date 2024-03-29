package com.feng.jdk.concurrency.patterns.masterslave.reusable;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Future;

/**
 * @author fengsy
 * @date 5/19/21
 * @Description
 */
public interface SubTaskDispatchStrategy<T, V> {
    /**
     * 根据指定的原始任务分解策略，将分解得来的各个子任务派发给一组Slave参与者实例。
     *
     * @param slaves             可以接受子任务的一组Slave参与者实例
     * @param taskDivideStrategy 原始任务分解策略
     * @return iterator。遍历该iterator可得到用于获取子任务处理结果的Promise（参见第6章，Promise模式）实例。
     * @throws InterruptedException 当Slave工作者线程被中断时抛出该异常。
     */
    Iterator<Future<V>> dispatch(Set<? extends SlaveSpec<T, V>> slaves, TaskDivideStrategy<T> taskDivideStrategy)
            throws InterruptedException;
}
