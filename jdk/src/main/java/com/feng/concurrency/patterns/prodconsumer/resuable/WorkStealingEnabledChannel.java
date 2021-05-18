package com.feng.concurrency.patterns.prodconsumer.resuable;

import java.util.concurrent.BlockingDeque;

/**
 * @author fengsy
 * @date 5/18/21
 * @Description
 */
public interface WorkStealingEnabledChannel<P> extends Channel<P> {
    P take(BlockingDeque<P> preferredQueue) throws InterruptedException;
}
