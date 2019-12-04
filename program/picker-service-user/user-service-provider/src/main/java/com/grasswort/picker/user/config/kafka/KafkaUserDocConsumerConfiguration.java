package com.grasswort.picker.user.config.kafka;

import com.grasswort.picker.user.dao.entity.User;
import com.grasswort.picker.user.dao.persistence.UserMapper;
import com.grasswort.picker.user.elastic.repository.UserDocRepository;
import com.grasswort.picker.user.service.elastic.UserDocConverter;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.Acknowledgment;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Optional;

/**
 * @author xuliangliang
 * @Classname KafkaBlogDocRefreshConsumer
 * @Description 博客更新消费
 * @Date 2019/12/3 17:32
 * @blame Java Team
 */
@Slf4j
@Configuration
public class KafkaUserDocConsumerConfiguration {

    @Autowired KafkaProperties kafkaProperties;

    @Resource UserMapper userMapper;

    @Resource UserDocRepository userDocRepository;

    @Resource UserDocConverter userDocConverter;


    @Bean
    public DefaultKafkaConsumerFactory kafkaUserDocUpdateConsumerFactory() {
        Map<String, Object> consumerProps = kafkaProperties.buildConsumerProperties();
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getCanonicalName());
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class.getCanonicalName());
        return new DefaultKafkaConsumerFactory<>(consumerProps);
    }

    /**
     * 消费者监听工厂
     * @return
     */
    @Bean
    public KafkaListenerContainerFactory userDocRefreshKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory conFactory = new ConcurrentKafkaListenerContainerFactory<>();
        conFactory.setConsumerFactory(kafkaUserDocUpdateConsumerFactory());
        //  设置消费者消费消息后的提交方式为手动提交
        conFactory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        return conFactory;
    }

    @KafkaListener(topicPattern = TopicUserDocUpdate.TOPIC, containerFactory = "userDocRefreshKafkaListenerContainerFactory")
    public void consumer(ConsumerRecord<String, Long> record, Acknowledgment acknowledgment) {
        log.info("\n主题：{}\n分区：{}\n偏移量：{}", record.topic(), record.partition(), record.offset());

        User user = Optional.ofNullable(record.value())
                .map(userMapper::selectByPrimaryKey)
                .orElse(null);
        if (user != null) {
            userDocRepository.save(userDocConverter.user2Doc(user));
        }
        acknowledgment.acknowledge();
    }


}
