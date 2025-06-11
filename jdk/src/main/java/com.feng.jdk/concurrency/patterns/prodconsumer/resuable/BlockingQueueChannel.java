package com.feng.jdk.concurrency.patterns.prodconsumer.resuable;

import java.util.concurrent.BlockingQueue;

/**
 * @author fengsy
 * @date 5/18/21
 * @Description
 */
public class BlockingQueueChannel<P> implements Channel<P> {

    private BlockingQueue<P> queue;

    public BlockingQueueChannel(BlockingQueue<P> queue) {
        this.queue = queue;
    }

    @Override
    public P take() throws InterruptedException {
        return queue.take();
    }

    @Override
    public void put(P product) throws InterruptedException {
        queue.put(product);
    }
}
