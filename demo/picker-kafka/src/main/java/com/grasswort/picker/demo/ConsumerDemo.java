package com.grasswort.picker.demo;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author xuliangliang
 * @Classname ConsumerDemo
 * @Description TODO
 * @Date 2019/10/3 22:46
 * @blame Java Team
 */
public class ConsumerDemo {

    /**
     * 重要参数介绍
     * 1.fetch.min.bytes 消费者从服务器获取记录的最小字节数。该值设置大一点可以降低 broker 的工作负载
     * 2.fetch.max.wait.ms  指定 broker 的等待时间，默认是 500ms, 和上一个参数相关。
     * 3.max.partition.fetch.bytes 服务器从每个分区里返回给消费者的最大字节数。它的默认值是 1M。该值必须比 broker 能够接收消息的最大字节数 大。
     * 4.session.timeout.ms 消费者被认为死亡之前可以与服务器断开连接的时间，默认是 3s。 与心跳参数 heartbeat.interval.ms 指定了心跳频率。一般设置时间比率大约 3:1
     * 5.auto.offset.reset 当偏移量无效时，消费者将如何处理 latest(默认，之后的) 和 earliest（起始）
     * 6.enable.auto.commit 是否自动提交偏移量，默认是 true。为了尽量避免出现重复数据和数据丢失，可以设置为 false, 由自己控制何时提交偏移量。还可以通过 auto.commit.interval.ms 来控制提交频率
     * 7.partition.assignment.strategy 分区分配策略。 Range (将若干连续的分区分配给消费者) RoundRobin (逐个分配给消费者)。也可以设置为自己的策略。
     * 8.client.id 可以是任意字符串， broker 用它来标识从客户端发送来的消息，通常被用在日志、度量指标、配额里。
     * 9.max.poll.records 单次调用 call() 方法，能够获取记录的数量。可以用来限制轮询里需要处理的数据量。
     * 10.receive.buffer.bytes 和 send.buffer.bytes  socket 在读写数据时用到的 TCP 缓冲区大小。如果被设置为 -1, 就使用系统默认值。
     * @param args
     */
    public static void main(String[] args) {
        Properties kafkaProps = new Properties();
        kafkaProps.put("bootstrap.servers", "182.92.3.187:9092,182.92.160.62:9092,39.96.42.239:9092");
        kafkaProps.put("group.id", "Test");
        kafkaProps.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        kafkaProps.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        kafkaProps.put("enable.auto.commit", false);
        // 创建消费者
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(kafkaProps);
        // 订阅主题
        consumer.subscribe(Collections.singletonList("test"));

        Map<TopicPartition, OffsetAndMetadata> currentOffSets = new HashMap<>();
        int count = 0;

        // consumer.seek(partitions, offset); 消费者可以自己选择开始读取的偏移量（如果本地存有偏移量）
        // 轮询
        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, String> record : records) {
                    System.out.println(String.format("主题：%s\n分区：%s\n偏移量：%s\nkey：%s\nvalue：%s",
                            record.topic(),
                            record.partition(),
                            record.offset(),
                            record.key(),
                            record.value()
                            ));

                    // 提交特定偏移量
                    /*currentOffSets.put(new TopicPartition(record.topic(), record.partition()), new OffsetAndMetadata(record.offset() + 1, "no metadata"));
                    if (count % 1000 == 0) {
                        consumer.commitAsync(currentOffSets, null);
                    }
                    count ++;*/
                }
                /*try {
                    // 同步提交偏移量，失败了会重试
                    consumer.commitSync();
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("提交偏移量失败！！！");
                }*/
                // 异步提交偏移量
                consumer.commitAsync();
            }
        } catch (WakeupException e) {
            // 在别的线程调用 consumer.wakeUp() 会导致该异常，类似 线程的 InterruptException
        } catch (Exception e) {
            System.out.println("Unexpected error " + e.getMessage());
        } finally {
            try {
                // 同步、异步组合提交
                consumer.commitSync();
            } finally {
                consumer.close();
            }
        }
    }
}
