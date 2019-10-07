package com.grasswort.picker.email.kafka;

import com.grasswort.picker.email.model.Mail;
import com.grasswort.picker.email.serializer.MailSerializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author xuliangliang
 * @Classname EmailKafkaProducer
 * @Description TODO
 * @Date 2019/10/6 21:39
 * @blame Java Team
 */
@Slf4j
@Component
public class EmailKafkaProducer {
    @Autowired
    KafkaProperties kafkaProperties;

    @Bean
    public KafkaEmailProducerFactory<String, Mail> kafkaEmailProducerFactory() {
        Map<String, Object> props = kafkaProperties.buildProducerProperties();
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getCanonicalName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, MailSerializer.class.getCanonicalName());
        return new KafkaEmailProducerFactory(props);
    }

    @Bean
    public KafkaTemplate<String, Mail> kafkaTemplate() {
        return new KafkaTemplate(kafkaEmailProducerFactory());
    }
}
