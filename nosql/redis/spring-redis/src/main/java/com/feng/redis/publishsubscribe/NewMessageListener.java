package com.feng.redis.publishsubscribe;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

/**
 * @Description 订阅redis中的消息
 * @Author fengsy
 * @Date 11/23/21
 */
@Component
@Slf4j
public class NewMessageListener implements MessageListener {
    StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
    private static final RedisSerializer<String> valueSerializer = new GenericToStringSerializer(Object.class);

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String topic = stringRedisSerializer.deserialize(message.getChannel());
        String jsonMsg = valueSerializer.deserialize(message.getBody());
        log.info("Message Received --> pattern: {}，topic:{}，message: {}", new String(pattern), topic, jsonMsg);
        JSONObject msgJson = JSONObject.parseObject(jsonMsg);
        long otherUid = msgJson.getLong("otherUid");
        JSONObject pushJson = new JSONObject();
        pushJson.put("type", 4);
        pushJson.put("data", msgJson);

        log.info("send to {} , msg: {}", otherUid, pushJson);
    }
}
