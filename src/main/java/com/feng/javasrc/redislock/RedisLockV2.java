package com.feng.javasrc.redislock;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

public class RedisLockV2 {

    public static Logger logger = LoggerFactory.getLogger(RedisLock.class);

    /**
     * 加锁标志
     */
    public static final String LOCKED = "TRUE";

    /**
     * 毫秒与毫微秒的换算单位 1毫秒 = 1000000毫微秒
     */
    public static final long MILLI_NANO_CONVERSION = 1000 * 1000L;

    /**
     * 默认超时时间（毫秒）
     */
    public static final long DEFAULT_TIME_OUT = 10000;

    /**
     * Random - 避免活锁
     */
    public static final Random RANDOM = new Random();

    /**
     * 锁的超时时间（秒）, 过期删除
     */
    public static final int EXPIRE = 3 * 60;

    /**
     * 切片
     */
    private ShardedJedisPool shardedJedisPool;

    /**
     * 单机
     */
    private ShardedJedis shardedJedis;

    /**
     * 非切片 - 单机池
     */
    private JedisPool redisPool;

    /**
     * 切片redis锁状态标志
     */
    private boolean shardedLocked = false;

    /**
     * 单机redis锁状态标志
     */
    private boolean singleLocked = false;

    /**
     * RedisLock. This creates a shared RedisLock -- 切片锁
     * 
     * @param shardedJedisPool
     */
    public RedisLockV2(final ShardedJedisPool shardedJedisPool) {
        this.shardedJedisPool = shardedJedisPool;
    }

    /**
     * This creates a single RedisLock -- 单机锁
     * 
     * @param redisPool
     */
    public RedisLockV2(final JedisPool redisPool) {
        try {
            this.redisPool = redisPool;
        } catch (Exception e) {
            logger.error("-- redis server connect exception!", e);
        }
    }

    /**
     * 获取jedis客户端
     * 
     * @return
     */
    public Jedis getRedisClient() {
        boolean isContinue = true;
        Jedis jedis = null;
        int count = 1;
        try {
            do {
                try {
                    jedis = redisPool.getResource();
                    isContinue = false;
                } catch (Exception e) {
                    isContinue = true;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    count++;
                }
                if (count > 3) {
                    break;
                }
            } while (isContinue);
        } catch (Exception e) {
            logger.error(" -- redis server connect exception-连接失败!", e);
        }
        if (null == jedis) {
            logger.warn("--- New jedisClient fail please check your redis config [检查redis配置]  -- ");
        }
        return jedis;
    }

    public void setShardedJedisPool(final ShardedJedisPool shardedJedisPool) {
        this.shardedJedisPool = shardedJedisPool;
    }

    public void setRedisPool(final JedisPool redisPool) {
        this.redisPool = redisPool;
    }

    /**
     * 单机redis加锁 singleLock(); try { doSomething(); } finally { singleLock();} 的方式调用
     * 
     * @param key
     * @param timeout
     *            超时时间
     * @return 成功或失败标志
     */
    public boolean singleLock(String key, long timeout) {
        Jedis singleJedis = getRedisClient();
        long nano = System.nanoTime();
        key = key.concat("_lock");
        timeout *= MILLI_NANO_CONVERSION;
        try {
            while ((System.nanoTime() - nano) < timeout) {
                if (singleJedis.setnx(key, LOCKED) == 1) {

                    singleJedis.expire(key, EXPIRE);
                    this.singleLocked = true;
                    return this.singleLocked;
                }
                // 短暂休眠，避免出现活锁 millis - nanos
                Thread.sleep(3, RANDOM.nextInt(500));
            }
        } catch (Exception e) {
            logger.error("-- singleLock Locking error: {} -----", e.getMessage());
        } finally {
            if (singleJedis != null) {
                try {
                    singleJedis.close();
                } catch (Exception e) {
                    logger.error("--- singleLock:finally.close:Error:{} ---", e.getMessage());
                }
            }
        }
        return false;
    }

    /**
     * 单机redis加锁 应该以：singleLock lock(); try { doSomething(); } finally { singleUnlock(); } 的方式调用
     * 
     * @param key
     * @param timeout
     * @param expire
     *            锁的超时时间（秒），过期删除
     * @return 成功或失败标志
     */
    public boolean singleLock(String key, long timeout, int expire) {
        Jedis singleJedis = getRedisClient();
        key = key.concat("_lock");
        long nano = System.nanoTime();
        timeout *= MILLI_NANO_CONVERSION;
        try {
            while ((System.nanoTime() - nano) < timeout) {
                if (singleJedis.setnx(key, LOCKED) == 1) {
                    singleJedis.expire(key, expire);
                    this.singleLocked = true;
                    return this.singleLocked;
                }
                // 短暂休眠，避免出现活锁
                Thread.sleep(3, RANDOM.nextInt(502));
            }
        } catch (Exception e) {
            throw new RuntimeException("---- SingleLock Locking error --- ", e);
        } finally {
            if (singleJedis != null) {
                try {
                    singleJedis.close();
                } catch (Exception e) {
                    logger.error("--- SingleLockError:{} ---", e.getMessage());
                }
            }
        }
        return false;
    }

    /**
     * 单机redis加锁 应该以：singleLock lock(); try { doSomething(); } finally { singleUnlock(); } 的方式调用
     * 
     * @return 成功或失败标志
     */
    public boolean singleLock(String key) {
        return singleLock(key, DEFAULT_TIME_OUT);
    }

    /**
     * 单机redis 释放锁 无论是否加锁成功，都需要调用unlock
     */
    public void singleUnlock(String key) {
        Jedis singleJedis = getRedisClient();
        key = key.concat("_lock");
        try {
            if (this.singleLocked) {
                singleJedis.del(key);
            }
        } finally {
            if (singleJedis != null) {
                try {
                    singleJedis.close();
                } catch (Exception e) {
                    logger.error("--- singleUnlockError:{} ---", e.getMessage());
                }
            }

        }
    }

    /**
     * 释放客户端
     * 
     * @param singleJedis
     */
    public void releaseResource(Jedis singleJedis) {
        if (singleJedis != null) {
            try {
                singleJedis.close();
            } catch (Exception e) {
                logger.error("释放jedis资源出错，将要关闭jedis，异常信息：" + e.getMessage());
                if (singleJedis != null) {
                    try {
                        singleJedis.disconnect();
                    } catch (Exception ex) {
                        logger.error("-- disconnect jedis connection fail: ", ex);
                    } finally {

                    }
                }
            }
        }
    }

    /******************************* sharded redis ******************************************/
    /**
     * 对切片redis群加锁 应该以：shardedLock lock(); try { doSomething(); } finally { shardedUnlock(); } 的方式调用
     * 
     * @param key
     * @param timeout
     *            超时时间
     * @return 成功或失败标志
     */
    public boolean shardedLock(String key, long timeout) {
        long nano = System.nanoTime();
        key = key.concat("_lock");
        timeout *= MILLI_NANO_CONVERSION;
        try {
            while ((System.nanoTime() - nano) < timeout) {
                if (this.shardedJedis.setnx(key, LOCKED) == 1) {
                    this.shardedJedis.expire(key, EXPIRE);
                    this.shardedLocked = true;
                    return this.shardedLocked;
                }
                /**
                 * 短暂休眠，避免出现活锁
                 */
                Thread.sleep(3, RANDOM.nextInt(555));
            }
        } catch (Exception e) {
            // throw new RuntimeException("Locking error", e);
            return false;
        }
        return false;
    }

    /**
     * 对切片redis群加锁 应该以： 应该以：shardedLock lock(); try { doSomething(); } finally { shardedUnlock(); } 的方式调用
     * 
     * @param timeout
     *            超时时间
     * @param expire
     *            锁的超时时间（秒），过期删除
     * @return 成功或失败标志
     */
    public boolean shardedLock(String key, long timeout, int expire) {
        key = key.concat("_lock");
        long nano = System.nanoTime();
        timeout *= MILLI_NANO_CONVERSION;
        try {
            while ((System.nanoTime() - nano) < timeout) {
                if (this.shardedJedis.setnx(key, LOCKED) == 1) {
                    this.shardedJedis.expire(key, expire);
                    this.shardedLocked = true;
                    return this.shardedLocked;
                }
                // 短暂休眠，避免出现活锁
                Thread.sleep(3, RANDOM.nextInt(500));
            }
        } catch (Exception e) {
            // throw new RuntimeException("Locking error", e);
            return false;
        }
        return false;
    }

    /**
     * 对切片redis群加锁 应该以：shardedLock lock(); try { doSomething(); } finally { shardedUnlock(); } 的方式调用
     * 
     * @return 成功或失败标志
     */
    public boolean shardedLock(String key) {
        return shardedLock(key, DEFAULT_TIME_OUT);
    }

    /**
     * 对切片redis群解锁 无论是否加锁成功，都需要调用unlock
     */
    @SuppressWarnings("deprecation")
    public void shardedUnlock(String key) {
        key = key.concat("_lock");
        try {
            if (this.shardedLocked) {
                this.shardedJedis.del(key);
            }
        } finally {
            this.shardedJedisPool.returnResource(this.shardedJedis);
        }
    }

}
