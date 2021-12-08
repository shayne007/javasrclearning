package com.feng.kafka.callback;

import java.util.Map;

import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.consumer.OffsetCommitCallback;
import org.apache.kafka.common.TopicPartition;

import lombok.extern.slf4j.Slf4j;

/**
 * @author fengsy
 * @date 3/26/21
 * @Description
 */
@Slf4j
public class LoggingOffsetCommitCallback implements OffsetCommitCallback {
    @Override
    public void onComplete(Map<TopicPartition, OffsetAndMetadata> offsets, Exception e) {
        if (e != null) {
            log.error("committ the offsets error", e);
        } else {
            log.debug("committed the offsets : {}", offsets);
        }
    }

}
