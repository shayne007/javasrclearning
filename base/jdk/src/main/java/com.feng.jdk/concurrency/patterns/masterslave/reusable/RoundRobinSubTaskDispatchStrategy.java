package com.feng.jdk.concurrency.patterns.masterslave.reusable;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;

/**
 * @author fengsy
 * @date 5/19/21
 * @Description
 */
public class RoundRobinSubTaskDispatchStrategy<T, V> implements SubTaskDispatchStrategy<T, V> {

    @Override
    public Iterator<Future<V>> dispatch(Set<? extends SlaveSpec<T, V>> slaves, TaskDivideStrategy<T> taskDivideStrategy)
            throws InterruptedException {
        final List<Future<V>> subResults = new LinkedList<Future<V>>();
        T subTask;

        Object[] arrSlaves = slaves.toArray();
        int i = -1;
        final int slaveCount = arrSlaves.length;
        Future<V> subTaskResultPromise;

        while (null != (subTask = taskDivideStrategy.nextChunk())) {
            i = (i + 1) % slaveCount;
            subTaskResultPromise = ((WorkerThreadSlave<T, V>) arrSlaves[i]).submit(subTask);
            subResults.add(subTaskResultPromise);
        }

        return subResults.iterator();
    }
}
