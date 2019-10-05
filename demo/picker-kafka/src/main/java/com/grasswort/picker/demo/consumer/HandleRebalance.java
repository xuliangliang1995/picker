package com.grasswort.picker.demo.consumer;

import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.common.TopicPartition;

import java.util.Collection;

/**
 * @author xuliangliang
 * @Classname HandleRebalance
 * @Description 消费组再均衡处理器
 * @Date 2019/10/4 15:25
 * @blame Java Team
 */
public class HandleRebalance implements ConsumerRebalanceListener {
    /**
     * 触发时机：再均衡开始之前、消费者停止读取消息之后
     * 如果在这里提交偏移量，下一个接管分区的消费者就知道从哪里开始读取啦 ~
     * @param collection
     */
    @Override
    public void onPartitionsRevoked(Collection<TopicPartition> collection) {
        System.out.println("Lost partition in rebalance. Commit current Offsets");
        // consumer.commitSync(currentOffSets);
    }

    /**
     * 触发时机：重新分配分区之后、消费者开始读取消息之前
     * @param collection
     */
    @Override
    public void onPartitionsAssigned(Collection<TopicPartition> collection) {
        // 如果偏移量由自己维护，可以在这里选择偏移量
    }
}
