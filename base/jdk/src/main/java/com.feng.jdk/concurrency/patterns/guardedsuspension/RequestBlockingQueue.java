package com.feng.jdk.concurrency.patterns.guardedsuspension;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author fengsy
 * @date 5/17/21
 * @Description
 */
public class RequestBlockingQueue {
    private BlockingQueue<Request> blockedQueue = new LinkedBlockingQueue<Request>();

    public Request getRequest() throws InterruptedException {
        return blockedQueue.take();
    }

    public void setRequest(Request request) throws InterruptedException {
        blockedQueue.put(request);
    }
}
