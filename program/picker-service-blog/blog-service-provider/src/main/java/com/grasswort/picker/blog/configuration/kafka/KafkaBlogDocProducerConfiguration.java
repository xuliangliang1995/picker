package com.grasswort.picker.blog.configuration.kafka;

import com.grasswort.picker.blog.constant.KafkaTemplateConstant;
import com.grasswort.picker.email.kafka.KafkaEmailProducerFactory;
import com.grasswort.picker.email.model.Mail;
import com.grasswort.picker.email.serializer.MailSerializer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Map;

/**
 * @author xuliangliang
 * @Classname KafkaBlogDocProducerConfiguration
 * @Description 博客更新生产者
 * @Date 2019/12/3 18:46
 * @blame Java Team
 */
@Configuration
public class KafkaBlogDocProducerConfiguration {

    @Autowired
    KafkaProperties kafkaProperties;

    @Bean
    public KafkaEmailProducerFactory<String, Long> kafkaBlogDocProducerFactory() {
        Map<String, Object> props = kafkaProperties.buildProducerProperties();
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getCanonicalName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, LongDeserializer.class.getCanonicalName());
        return new KafkaEmailProducerFactory(props);
    }

    @Bean(KafkaTemplateConstant.BLOG_DOC_UPDATE)
    public KafkaTemplate<String, Long> kafkaTemplate() {
        return new KafkaTemplate(kafkaBlogDocProducerFactory());
    }
}
