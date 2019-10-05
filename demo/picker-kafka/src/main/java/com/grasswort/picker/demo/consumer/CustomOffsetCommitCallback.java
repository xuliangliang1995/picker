package com.grasswort.picker.demo.consumer;

import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.consumer.OffsetCommitCallback;
import org.apache.kafka.common.TopicPartition;

import java.util.Map;

/**
 * @author xuliangliang
 * @Classname CustomOffsetCommitCallback
 * @Description TODO
 * @Date 2019/10/4 9:56
 * @blame Java Team
 */
public class CustomOffsetCommitCallback implements OffsetCommitCallback {
    @Override
    public void onComplete(Map<TopicPartition, OffsetAndMetadata> map, Exception e) {
        if (e != null) {
            System.out.println("提交偏移量失败！" + e);
        }
    }
}
