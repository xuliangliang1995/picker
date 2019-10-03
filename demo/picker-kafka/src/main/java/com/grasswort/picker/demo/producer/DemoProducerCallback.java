package com.grasswort.picker.demo.producer;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.RecordMetadata;

/**
 * @author xuliangliang
 * @Classname DemoProducerCallback
 * @Description 生产者发送消息成功回调
 * @Date 2019/10/3 17:31
 * @blame Java Team
 */
public class DemoProducerCallback implements Callback {
    @Override
    public void onCompletion(RecordMetadata recordMetadata, Exception e) {
        System.out.println("异步发送消息成功回调");
        System.out.println("[异]主题：" + recordMetadata.topic());
        System.out.println("[异]分区：" + recordMetadata.partition());
        System.out.println("[异]偏移量：" + recordMetadata.offset());
    }
}
