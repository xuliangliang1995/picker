package com.grasswort.picker.email.kafka;

import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.Map;

/**
 * @author xuliangliang
 * @Classname KafkaEmailConsumerFactory
 * @Description kafka Email 消费者工厂
 * @Date 2019/10/6 19:04
 * @blame Java Team
 */
public class KafkaEmailConsumerFactory extends DefaultKafkaConsumerFactory {
    public KafkaEmailConsumerFactory(Map configs) {
        super(configs);
    }
}
