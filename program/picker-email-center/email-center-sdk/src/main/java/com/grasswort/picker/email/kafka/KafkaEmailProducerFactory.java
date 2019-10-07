package com.grasswort.picker.email.kafka;

import org.springframework.kafka.core.DefaultKafkaProducerFactory;

import java.util.Map;

/**
 * @author xuliangliang
 * @Classname KafkaEmailProducerFactory
 * @Description TODO
 * @Date 2019/10/6 21:39
 * @blame Java Team
 */
public class KafkaEmailProducerFactory<K, V> extends DefaultKafkaProducerFactory<K, V> {
    public KafkaEmailProducerFactory(Map configs) {
        super(configs);
    }
}
