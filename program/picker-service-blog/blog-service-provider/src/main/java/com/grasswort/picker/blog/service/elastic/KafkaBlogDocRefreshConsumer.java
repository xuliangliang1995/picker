package com.grasswort.picker.blog.service.elastic;

import com.grasswort.picker.blog.dao.entity.Blog;
import com.grasswort.picker.blog.dao.persistence.BlogMapper;
import com.grasswort.picker.blog.elastic.repository.BlogDocRepository;
import com.grasswort.picker.email.constants.EmailCenterConstant;
import com.grasswort.picker.email.serializer.MailDeserializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
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
public class KafkaBlogDocRefreshConsumer {

    @Autowired KafkaProperties kafkaProperties;

    @Autowired BlogDocRepository blogDocRepository;

    @Resource BlogMapper blogMapper;

    @Autowired BlogDocConverter blogDocConverter;

    @Bean
    public DefaultKafkaConsumerFactory kafkaEmailConsumerFactory() {
        Map<String, Object> consumerProps = kafkaProperties.buildConsumerProperties();
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getCanonicalName());
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, MailDeserializer.class.getCanonicalName());
        return new DefaultKafkaConsumerFactory<>(consumerProps);
    }

    /**
     * 消费者监听工厂
     * @return
     */
    @Bean
    public KafkaListenerContainerFactory blogDocRefreshKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory conFactory = new ConcurrentKafkaListenerContainerFactory<>();
        conFactory.setConsumerFactory(kafkaEmailConsumerFactory());
        //  设置消费者消费消息后的提交方式为手动提交
        conFactory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        return conFactory;
    }

    @KafkaListener(topicPattern = EmailCenterConstant.LISTENING_TOPIC, containerFactory = "blogDocRefreshKafkaListenerContainerFactory")
    public void consumer(ConsumerRecord<String, Long> record, Acknowledgment acknowledgment) {
        log.info("\n主题：{}\n分区：{}\n偏移量：{}", record.topic(), record.partition(), record.offset());

        Blog blog = Optional.ofNullable(record.value())
                .map(blogId -> blogMapper.selectByPrimaryKey(blogId))
                .orElse(null);
        if (blog != null) {
            blogDocRepository.save(blogDocConverter.blog2BlogDoc(blog));
        }

        acknowledgment.acknowledge();
    }


}
