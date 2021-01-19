package com.fsy.javasrc.threads;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author fengsy
 * @date 1/12/21
 * @Description
 */

class Allocator {
    private List<Account> als = new ArrayList<>();

    /**
     * 一次性申请所有资源
     * 
     * @param from
     * @param to
     */
    synchronized void apply(Account from, Account to) {
        // 经典写法
        while (als.contains(from) || als.contains(to)) {
            try {
                System.out.println("等待用户 -> " + from.getId() + "_" + to.getId());
                wait();
            } catch (InterruptedException e) {
                System.out.println("异常用户 -> " + from.getId() + "_" + to.getId());
                e.printStackTrace();
            }
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        als.add(from);
        als.add(to);
    }

    /**
     * 归还资源
     * 
     * @param from
     * @param to
     */
    synchronized void free(Account from, Account to) {
        System.out.println("唤醒用户 -> " + from.getId() + "_" + to.getId());
        als.remove(from);
        als.remove(to);
        notifyAll();
    }
}

class Account {
    private final long id;
    private final Allocator allocator;

    private int balance;

    public Account(Allocator allocator, long id, int balance) {
        this.allocator = allocator;
        this.id = id;
        this.balance = balance;
    }

    public long getId() {
        return id;
    }

    /**
     * 转账
     * 
     * @param target
     * @param amt
     */
    public void transfer(Account target, int amt) {
        // 一次性申请转出账户和转入账户，直到成功
        allocator.apply(this, target);
        try {
            // TODO 有了资源管理器，这里的synchronized锁就不需要了！
            if (this.balance > amt) {
                this.balance -= amt;
                target.balance += amt;
            }
            // 模拟数据库操作时间
            try {
                Thread.sleep(new Random().nextInt(2000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } finally {
            allocator.free(this, target);
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int)(id ^ (id >>> 32));
        return result;
    }

    /**
     * 用于判断两个用户是否一致
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Account other = (Account)obj;
        if (id != other.id)
            return false;
        return true;
    }
}