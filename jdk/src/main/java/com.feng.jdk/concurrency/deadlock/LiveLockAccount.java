package com.feng.jdk.concurrency.deadlock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description TODO
 * @Author fengsy
 * @Date 11/13/21
 */

class LiveLockAccount {
    private int balance;
    private final Lock lock = new ReentrantLock();

    // 转账
    void transfer(LiveLockAccount target, int amt) {
        while (true) {
            if (this.lock.tryLock()) {
                try {
                    if (target.lock.tryLock()) {
                        try {
                            this.balance -= amt;
                            target.balance += amt;
                        } finally {
                            target.lock.unlock();
                        }
                    }//if
                } finally {
                    this.lock.unlock();
                }
            }//if
        }//while
    }//transfer
}