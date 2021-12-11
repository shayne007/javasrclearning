package com.feng.redis.cache;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author fengsy
 * @date 7/28/21
 * @Description
 */
@Service
@Slf4j
public class CacheService {
    private static final String CONSUME_FLAG = "person_";
    @Resource
    RedisTemplate<String, Object> redisTemplate;

    public void check() {
        Set<String> keys = redisTemplate.keys("*");
        if (keys == null) {
            return;
        }
        List<String> msgs = Lists.newArrayList();
        keys.stream().filter(key -> key.startsWith(CONSUME_FLAG)).forEach(key -> {
            Boolean setNX = redisTemplate.opsForValue().setIfAbsent(CONSUME_FLAG + key, "1", 60,
                    TimeUnit.SECONDS);
            if (setNX != null && setNX) {
                Person msg = (Person) redisTemplate.opsForValue().get(key);
                if (!StringUtils.isEmpty(msg)) {
                    msgs.add(msg.name);
                    redisTemplate.delete(Lists.newArrayList(key, CONSUME_FLAG + key));
                    log.info("id {} is added. msg is {}", key, msg.getName());
                } else {
                    log.error("id {} value is null", key);
                }
            } else {
                log.info("id {} is already consumed", key);
            }
        });
        if (!msgs.isEmpty()) {
            Iterable<List<String>> partition = Iterables.partition(msgs, 10);
            partition.forEach(System.out::println);
        }
    }

    public void saveCache(List<Person> data) {

        redisTemplate.executePipelined(new RedisCallback<Object>() {
            RedisSerializer keyS = redisTemplate.getKeySerializer();
            RedisSerializer valueS = redisTemplate.getValueSerializer();

            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                for (Person person : data) {
                    connection.set(keyS.serialize("person_" + person.getId()), valueS.serialize(person));

                    connection.set(keyS.serialize("address_" + person.getName()), valueS.serialize(person));
                }
                return null;
            }
        });
    }

    public static class Person implements Serializable {
        private Long id;
        private String name;

        public Person() {
        }

        public Person(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }
}
