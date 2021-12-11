package com.feng.kafka.async;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;

/**
 * @Description TODO
 * @Author fengsy
 * @Date 11/1/21
 */
@RestController
@Slf4j
public class MessageController {

    @Resource
    private KafkaTemplate<String, String> template;


    @GetMapping("/send/{input}")
    public void send(@PathVariable String input) {
        JSONObject userData = new JSONObject();
        userData.put("operate", "add");
        ArrayList<UserDTO> list = new ArrayList<>();
        list.add(new UserDTO());
        list.add(new UserDTO(input, 22));
        userData.put("data", list);

        this.template.send("topic-test", userData.toJSONString())
                .addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
                    @Override
                    public void onFailure(Throwable e) {
                        log.error("send message error[{}] ", input);
                    }

                    @Override
                    public void onSuccess(SendResult<String, String> objectObjectSendResult) {
                        log.info("回调结果 Result =  topic:[{}] , partition:[{}], offset:[{}]",
                                objectObjectSendResult.getRecordMetadata().topic(),
                                objectObjectSendResult.getRecordMetadata().partition(),
                                objectObjectSendResult.getRecordMetadata().offset());
                    }
                });
    }
}
