package com.feng.redis.publishsubscribe;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Description TODO
 * @Author fengsy
 * @Date 11/23/21
 */
@Service
public class MessageServiceImpl {
    @Autowired
    RedisTemplate redisTemplate;

    public MessageVO sendNewMsg(long senderUid, long recipientUid, String content, int msgType) {
        MessageVO messageVO = new MessageVO(1001L, content, 1L, msgType, 2L,
                new Date(), "", "", "uname", "otheruname");
        redisTemplate.convertAndSend(Constants.WEBSOCKET_MSG_TOPIC, JSONObject.toJSONString(messageVO));

        return messageVO;
    }
}
