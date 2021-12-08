package com.feng.jdk.concurrency.livelock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description TODO
 * @Author fengsy
 * @Date 11/13/21
 */

class Account {
    private int balance;
    private final Lock lock = new ReentrantLock();

    // 转账
    void transfer(Account tar, int amt) {
        while (true) {
            if (this.lock.tryLock()) {
                try {
                    if (tar.lock.tryLock()) {
                        try {
                            this.balance -= amt;
                            tar.balance += amt;
                        } finally {
                            tar.lock.unlock();
                        }
                    }//if
                } finally {
                    this.lock.unlock();
                }
            }//if
        }//while
    }//transfer
}