package com.grasswort.picker.demo.producer;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.utils.Utils;

import java.util.List;
import java.util.Map;

/**
 * @author xuliangliang
 * @Classname CustomPartitioner
 * @Description  自定义分区器
 * @Date 2019/10/3 19:28
 * @blame Java Team
 */
public class CustomPartitioner implements Partitioner {
    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        List<PartitionInfo> partitions = cluster.partitionsForTopic(topic);
        int numPartitions = partitions.size();

        if (keyBytes == null || ! (key instanceof String)) {
            throw new RuntimeException("We expected all messages to have customer name as key");
        }

        if (((String) key).equals("Grasswort")) {
            return numPartitions;
        }
        return Math.abs(Utils.murmur2(keyBytes)) % (numPartitions -1);
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}
