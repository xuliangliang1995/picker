package com.grasswort.picker.user.config.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author xuliangliang
 * @Classname KafkaProducerConfiguration
 * @Description kafka 配置
 * @Date 2019/10/6 21:29
 * @blame Java Team
 */
@Slf4j
@Configuration
public class KafkaTopicConfiguration {

    @Bean
    public NewTopic userActivateEmailTopic(@Autowired TopicActivateEmail topicActivateEmail) {
        log.info("\n[TopicConfiguration]{}", topicActivateEmail);
        return new NewTopic(topicActivateEmail.getTopicName(), topicActivateEmail.getPartitions(), topicActivateEmail.getReplicationFactor());
    }

    @Bean
    public NewTopic userCaptchaTopic(@Autowired TopicCaptcha topicCaptcha) {
        log.info("\n[TopicConfiguration]{}", topicCaptcha);
        return new NewTopic(topicCaptcha.getTopicName(), topicCaptcha.getPartitions(), topicCaptcha.getReplicationFactor());
    }
}
