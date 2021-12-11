package com.feng.jdk.concurrency.patterns.stconfinement.reusable;

/**
 * @author fengsy
 * @date 5/19/21
 * @Description
 */
public interface TaskProcessor<T, V> {
    /**
     * 对指定任务进行处理。
     *
     * @param task 任务
     * @return 任务处理结果
     * @throws Exception
     */
    V doProcess(T task) throws Exception;
}
