package com.grasswort.picker.demo;

import com.grasswort.picker.demo.producer.DemoProducerCallback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.Future;

/**
 * @author xuliangliang
 * @Classname App
 * @Description TODO
 * @Date 2019/10/3 14:01
 * @blame Java Team
 */
@SpringBootApplication
public class App {
    /**
     * 发送消息的三种方式
     * 1. 发送并忘记 fire-and-forget
     * 2. 同步发送 send  Future
     * 3. 异步发送 send  Callback
     *
     *         // 重要的参数
     *         // acks 0, 1, all
     *         // buffer.memory producer 缓冲区大小
     *         // compression.type 压缩类型 snappy/gzip/lz4
     *         // retries 重试次数
     *         // batch.size 批次可以使用的内存大小
     *         // linger.ms 发送批次之前，等待更多消息加入批次的时间
     *         // client.id 任意字符串，可以用来标识消息的来源
     *         // max.in.flight.requests.per.connection 生产者在收到服务器响应之前可以发送多少消息。增大会提高吞吐量，等于1可以保证消息顺序
     *         // timeout.ms  broker 等待同步副本返回消息确认的时间，与 acks 的配置想匹配，如果在指定时间内没有收到同步副本的确认，broker 会返回一个错误
     *         // request.timeout.ms 生产者发送数据时等待服务器返回响应的时间
     *         // fetch.timeout.ms 生产者获取元数据时（判断哪个分区是首领），等待服务器响应的时间
     *         // max.block.ms 当缓冲区满了的时候，允许阻塞的时长，超时则报错
     *         // mex.request.size 生产者发送请求的大小
     *         // receive.buffer.bytes 和 send.buffer.bytes TCP socket 接收和发送数据包的缓冲区大小
     *
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(App.class);
        Properties kafkaProps = new Properties();
        kafkaProps.put("bootstrap.servers", "182.92.3.187:9092,182.92.160.62:9092,39.96.42.239:9092");
        kafkaProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        kafkaProps.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        KafkaProducer<String, String> producer = new KafkaProducer<>(kafkaProps);
        ProducerRecord<String, String> news = new ProducerRecord<>("test", "owner", "xuliangliang");
        try {
            // 同步发送
            Future<RecordMetadata> future = producer.send(news);
            RecordMetadata metadata = future.get();
            System.out.println("主题：" + metadata.topic());
            System.out.println("分区：" + metadata.partition());
            System.out.println("偏移量：" + metadata.offset());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            // 异步发送
            producer.send(news, new DemoProducerCallback());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
