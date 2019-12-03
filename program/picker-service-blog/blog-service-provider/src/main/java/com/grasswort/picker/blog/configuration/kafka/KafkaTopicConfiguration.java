package com.grasswort.picker.blog.configuration.kafka;

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
    public NewTopic userActivateEmailTopic(@Autowired TopicBlogPush topicBlogPush) {
        log.info("\n[TopicConfiguration]{}", topicBlogPush);
        return new NewTopic(topicBlogPush.getTopicName(), topicBlogPush.getPartitions(), topicBlogPush.getReplicationFactor());
    }

    @Bean
    public NewTopic blogUpdateTopic(@Autowired TopicUpdateBlogDoc topicUpdateBlogDoc) {
        log.info("\n[TopicConfiguration]{}", topicUpdateBlogDoc);
        return new NewTopic(topicUpdateBlogDoc.getTopicName(), topicUpdateBlogDoc.getPartitions(), topicUpdateBlogDoc.getReplicationFactor());
    }

}
