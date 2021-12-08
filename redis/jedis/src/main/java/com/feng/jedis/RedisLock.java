package com.feng.jedis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author fengsy
 * @date 2/19/21
 * @Description
 */

public class RedisLock {
    private Jedis jedis;

    public RedisLock(Jedis jedis) {
        this.jedis = jedis;
    }

    // 获取锁
    public synchronized boolean lock(String lockId) {
        // 设置锁
        Long status = jedis.setnx(lockId, System.currentTimeMillis() + "");
        if (0 == status) {
            return false;
        } else {
            return true;
        }
    }

    // 释放锁
    public synchronized boolean unlock(String lockId) {
        String lockValue = jedis.get(lockId);
        if (lockValue != null) {
            jedis.del(lockId);
            return true;
        } else {
            return false;
        }
    }

    public static void main(String[] args) {

        for (int i = 0; i < 3; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    execute();
                }
            }).start();;
        }
    }

    private static void execute() {
        JedisPoolConfig jcon = new JedisPoolConfig();
        JedisPool jp = new JedisPool(jcon, "127.0.0.1", 6379);
        Jedis jedis = jp.getResource();

        RedisLock lock = new RedisLock(jedis);
        String lockId = "123";
        try {
            if (lock.lock(lockId)) {
                // 加锁后需要执行的逻辑代码
                System.out.println(Thread.currentThread().getName() + " 线程加锁成功，执行业务代码。。。");
                Thread.sleep(300);
            } else {
                Thread.sleep(500);
                if (lock.lock(lockId)) {
                    // 加锁后需要执行的逻辑代码
                    System.out.println(Thread.currentThread().getName() + " 线程加锁成功，执行业务代码。。。");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock(lockId);
        }
    }
}
