package com.feng.redis.publishsubscribe;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * @Description TODO
 * @Author fengsy
 * @Date 11/23/21
 */
@RestController("/message")
public class MessageController {
    @Autowired
    MessageServiceImpl messageService;

    @PostMapping(path = "/sendMsg")
    public String sendMsg(@RequestParam Long senderUid, @RequestParam Long recipientUid, String content, Integer msgType, Model model, HttpSession session) {
        MessageVO messageContent = messageService.sendNewMsg(senderUid, recipientUid, content, msgType);
        if (null != messageContent) {
            return JSONObject.toJSONString(messageContent);
        } else {
            return "";
        }
    }
}
