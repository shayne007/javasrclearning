package com.feng.redis.publishsubscribe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @Description TODO
 * @Author fengsy
 * @Date 11/23/21
 */
@Configuration
public class SpringSessionRedisConfiguration {
    @Value("${spring.redis.host}")
    private String redisHostName;

    @Value("${spring.redis.port}")
    private int redisPort;

    @Value("${spring.redis.password}")
    private String password;

    @Autowired
    private NewMessageListener newMessageListener;

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {

        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(redisHostName);
        config.setPort(redisPort);
        config.setPassword(RedisPassword.of(password));
        config.setDatabase(0);
        JedisConnectionFactory factory = new JedisConnectionFactory(config);
        factory.setUsePool(true);
        return factory;
    }

    @Bean
    RedisTemplate<Object, Object> redisTemplate() {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<Object, Object>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory());
        GenericToStringSerializer genericToStringSerializer = new GenericToStringSerializer(Object.class);

        redisTemplate.setValueSerializer(genericToStringSerializer);
        redisTemplate.setKeySerializer(new StringRedisSerializer());

        redisTemplate.setHashKeySerializer(genericToStringSerializer);
        redisTemplate.setHashValueSerializer(genericToStringSerializer);

        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

//    @Bean
//    RedisCacheManager cacheManager() {
//        RedisCacheManager redisCacheManager = new RedisCacheManager(redisTemplate());
//        return redisCacheManager;
//    }

    /**
     * 用于消息监听，需要将 Topic 和 MessageListener 注册到 RedisMessageListenerContainer 中。
     * 这样，当 Topic 上有消息时，由 RedisMessageListenerContainer 通知 MessageListener，
     * 客户端通过 onMessage 拿到消息后，自行处理。
     *
     * @return
     */

    @Bean
    RedisMessageListenerContainer redisContainer() {
        final RedisMessageListenerContainer container = new RedisMessageListenerContainer();

        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(new MessageListenerAdapter(newMessageListener),
                new ChannelTopic(Constants.WEBSOCKET_MSG_TOPIC));

        return container;
    }
}
