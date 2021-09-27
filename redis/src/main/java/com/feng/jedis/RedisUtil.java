package com.feng.jedis;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * shared jedis pool util 分片连接池
 *
 * @author fengsy
 * @date 7/29/21
 * @Description
 */
@Slf4j
public class RedisUtil {
    private static JedisPool jedisPool;
    private static final int MAX_IDLE = 50;
    private static final int MAX_TOTAL = 50;
    private static final int MIN_IDLE = 2;

    @PostConstruct
    private void init() {
        buidJedisPool();
        hotPool();
    }

    static {
        buidJedisPool();
        hotPool();
    }

    /**
     * 根据参数配置创建Jedis Pool
     */
    public static void buidJedisPool() {
        long start = System.currentTimeMillis();
        JedisPoolConfig config = new JedisPoolConfig();

        // 设置最大对象数
        config.setMaxTotal(MAX_TOTAL);
        // 最大能够保持空闲状态的对象数
        config.setMaxIdle(MAX_IDLE);
        config.setMinIdle(MIN_IDLE);

        // 当资源耗尽时，调用者阻塞等待,默认为true
        config.setBlockWhenExhausted(true);
        // 等待超时时间
        config.setMaxWaitMillis(10 * 1000);

        // 在获取连接的时候检查有效性, 默认false
        config.setTestOnBorrow(true);
        // 在返回Object时, 对返回的connection进行validateObject校验
        config.setTestOnReturn(true);
        // 创建新的连接时检查有效性, 默认false
        config.setTestOnCreate(true);

        // config.setTestWhileIdle(true);
        // config.setTimeBetweenEvictionRunsMillis(30 * 1000);

        jedisPool = new JedisPool(config, "127.0.0.1", 6379, 5000, "123456");
        // jedisPool = new JedisPool(config, "110.42.251.23", 6379);

        long end = System.currentTimeMillis();
        log.info("create jedis pool cost time: {} ms", (end - start));
    }

    /**
     * 根据最小空闲连接数预热jedisPool
     */
    public static void hotPool() {
        long start = System.currentTimeMillis();
        List<Jedis> minIdleJedisList = new ArrayList<>(MIN_IDLE);
        Jedis jedis = null;
        for (int i = 0; i < MIN_IDLE; i++) {
            try {
                jedis = jedisPool.getResource();
                minIdleJedisList.add(jedis);
                jedis.ping();
            } catch (Exception e) {
                log.error(e.getMessage());
            } finally {

            }

        }

        for (int i = 0; i < MIN_IDLE; i++) {
            try {
                jedis = minIdleJedisList.get(i);
                jedis.close();
            } catch (Exception e) {
                log.error(e.getMessage());
            } finally {

            }

        }
        long end = System.currentTimeMillis();
        log.info("hot pool cost time: {} ms", (end - start));
    }

    /**
     * 从连接池中获取一个ShardedJedis对象
     */
    public static Jedis getJedis() {
        Jedis jedis = jedisPool.getResource();
        return jedis;
    }

    /**
     * 删除某个key
     *
     * @param key
     * @return
     */
    public static Long deleteKey(String key) {

        Long del = null;
        try (Jedis jedis = getJedis()) {
            del = jedis.del(key);
        } catch (Exception e) {
            log.error("==== RedisUtil.deleteKey() Exception", e);
        }
        return del;
    }

    /**
     * 根据key获取value
     *
     * @param key
     * @return
     */
    public static String getValueByKey(String key) {
        Jedis jedis = getJedis();
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
        Jedis jedis = getJedis();
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

    public static void main(String[] args) throws InterruptedException {
        // new RedisUtil().init();
        long start = System.currentTimeMillis();
        addValue("k", "testvvv");
        String k = getValueByKey("k");
        long end = System.currentTimeMillis();
        log.info("execute commands cost time: {} ms", (end - start));
        System.out.println(k);
    }
}
