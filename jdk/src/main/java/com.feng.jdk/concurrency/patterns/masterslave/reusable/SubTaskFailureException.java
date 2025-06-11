package com.feng.jdk.concurrency.patterns.masterslave.reusable;

/**
 * @author fengsy
 * @date 5/19/21
 * @Description
 */
public class SubTaskFailureException extends Exception {
    /**
     * 对处理失败的子任务进行重试所需的信息
     */
    @SuppressWarnings("rawtypes")
    public final RetryInfo retryInfo;

    @SuppressWarnings("rawtypes")
    public SubTaskFailureException(RetryInfo retryInfo, Exception cause) {
        super(cause);
        this.retryInfo = retryInfo;
    }
}
