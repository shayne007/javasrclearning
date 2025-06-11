package com.feng.jedis;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

/**
 * shared jedis pool util 分片连接池
 * 
 * @author fengsy
 * @date 7/29/21
 * @Description
 */
@Slf4j
public class SharedJedisPoolUtil {
    private static ShardedJedisPool jedisPool;
    private static final int MAX_IDLE = 50;
    private static final int MAX_TOTAL = 50;

    @PostConstruct
    private void init() {
        JedisPoolConfig config = new JedisPoolConfig();

        // 设置最大对象数
        config.setMaxTotal(MAX_TOTAL);
        // 最大能够保持空闲状态的对象数
        config.setMaxIdle(MAX_IDLE);
        // jedisPoolConfig.setMinIdle(2);

        // 超时时间
        config.setMaxWaitMillis(10 * 1000);

        // 在获取连接的时候检查有效性, 默认false
        config.setTestOnBorrow(true);
        // 在返回Object时, 对返回的connection进行validateObject校验
        config.setTestOnReturn(true);
        config.setTestOnCreate(true);

        // 如果是集群，可以全部加入list中
        List<JedisShardInfo> shardInfos = new ArrayList<JedisShardInfo>(2);
        JedisShardInfo shardInfoA = new JedisShardInfo("redis://:@127.0.0.1:6379/1");
        shardInfoA.setPassword("123456");
        JedisShardInfo shardInfoB = new JedisShardInfo("110.42.251.23", "6379");
        // shardInfoB.setPassword("123456");
        // shardInfos.add(shardInfoA);
        shardInfos.add(shardInfoB);
        jedisPool = new ShardedJedisPool(config, shardInfos);
    }

    /**
     * 从连接池中获取一个ShardedJedis对象
     */
    public static ShardedJedis getJedis() {
        return jedisPool.getResource();
    }

    /**
     * 根据key获取value
     *
     * @param key
     * @return
     */
    public static String getValueByKey(String key) {
        ShardedJedis jedis = getJedis();
        String value = null;
        try {
            value = jedis.get(key);
        } catch (Exception e) {
            log.error("==== RedisUtil.getValueByKey() Exception", e);
        } finally {
            jedis.close();
        }
        return value;
    }

    /**
     * 设置redis数据
     *
     * @param key
     * @param value
     * @return
     */
    public static String addValue(String key, String value) {
        ShardedJedis jedis = getJedis();
        String setFlag = null;
        try {
            setFlag = jedis.set(key, value);
        } catch (Exception e) {
            log.error("==== RedisUtil.addValue() Exception", e);
        } finally {
            jedis.close();
        }
        return setFlag;
    }

    public static void main(String[] args) {
        new SharedJedisPoolUtil().init();
        addValue("k", "testvvv");
        String k = getValueByKey("k");
        System.out.println(k);
    }
}
