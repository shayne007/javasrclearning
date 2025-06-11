package com.feng.kafka.async;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @Description TODO
 * @Author fengsy
 * @Date 11/1/21
 */
@Component
@Slf4j
public class KafkaConsumer {

    @Autowired
    UserService userService;

    @KafkaListener(topics = {"topic-test"}, containerFactory = "batchFactory", groupId = "group-test")
    public void listenCluster(List<ConsumerRecord<String, String>> records, Acknowledgment ack) {

        Long time1 = System.currentTimeMillis();
        List<UserDTO> userDTOList = Lists.newArrayList();
        for (ConsumerRecord<String, String> record : records) {
            JSONObject recordJson = JSON.parseObject(record.value());
            JSONArray data = recordJson.getJSONArray("data");
            userDTOList.addAll(data.toJavaList(UserDTO.class));
        }
        CountDownLatch countDownLatch = new CountDownLatch(userDTOList.size());
        for (UserDTO clusterDTO : userDTOList) {
            userService.handleData(clusterDTO, countDownLatch);
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Long time2 = System.currentTimeMillis();
        Long costTime = time2 - time1;
        log.info("处理{}条消息中的{}条数据耗时：{}ms", records.size(), userDTOList.size(), costTime);
        ack.acknowledge();
    }

}
