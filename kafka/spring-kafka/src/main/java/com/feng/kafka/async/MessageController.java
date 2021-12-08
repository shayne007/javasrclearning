package com.feng.kafka.deadletter;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

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
        JSONObject clusterData = new JSONObject();
        clusterData.put("operate", "add");
        clusterData.put("data", input);

        this.template.send("topic-testdlt", clusterData.toJSONString())
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

    @KafkaListener(id = "webGroup", topics = "topic-testdlt", containerFactory = "containerFactory", groupId = "group-test")
    public void listen(List<ConsumerRecord<String, String>> records, Acknowledgment ack) {
        log.info("records count: {}", records.size());
        for (ConsumerRecord<String, String> record : records) {
            log.info("input key: {}, value: {}", record.key(), record.value());
        }

//        throw new RuntimeException("dlt");
    }

    @KafkaListener(id = "dltGroup", topics = "topic-testdlt.DLT", groupId = "group-test")
    public void dltListen(String input, Acknowledgment ack) {
        log.info("receive from DLT, value: {}", input);
        ack.acknowledge();
    }
}
