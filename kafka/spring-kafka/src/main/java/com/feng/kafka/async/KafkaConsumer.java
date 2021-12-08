package com.jiuling.archives.support;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.jiuling.archives.domain.dto.ClusterDTO;
import com.jiuling.archives.service.IQstArchivesService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author lanjian
 * @date 2020/1/15 6:54 下午
 */
@Component
@Slf4j
public class KafkaConsumer {

    @Autowired
    IQstArchivesService qstArchivesService;

    @Autowired
    Executor asyncExecutor;

    @KafkaListener(topics = {"receive_cluster_topic"}, containerFactory = "batchFactory")
    public void listenCluster(List<ConsumerRecord<String, String>> records, Acknowledgment ack) {
        Long time1 = System.currentTimeMillis();
        List<ClusterDTO> clusterDTOList = Lists.newArrayList();
        for (ConsumerRecord<String, String> record : records) {
            JSONObject recordJson = JSON.parseObject(record.value());
            JSONArray data = recordJson.getJSONArray("data");
            clusterDTOList.addAll(data.toJavaList(ClusterDTO.class));
        }
        CountDownLatch countDownLatch = new CountDownLatch(clusterDTOList.size());
        for (ClusterDTO clusterDTO : clusterDTOList) {
            qstArchivesService.handleClusterData(clusterDTO, countDownLatch);
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Long time2 = System.currentTimeMillis();
        Long costTime = time2 - time1;
        log.info("处理50条数据耗时：{}ms", costTime);
        ack.acknowledge();
    }

}
