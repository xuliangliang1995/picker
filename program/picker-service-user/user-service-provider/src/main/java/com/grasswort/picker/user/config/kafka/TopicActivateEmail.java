package com.grasswort.picker.user.config.kafka;

import com.alibaba.nacos.api.config.ConfigType;
import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import lombok.Getter;
import lombok.ToString;
import org.springframework.stereotype.Component;

/**
 * @author xuliangliang
 * @Classname TopicActivateEmail
 * @Description 激活邮件主题
 * @Date 2019/10/10 14:02
 * @blame Java Team
 */
@Getter
@ToString
@Component
@NacosPropertySource(dataId = "kafka-topics", type = ConfigType.PROPERTIES)
public class TopicActivateEmail {
    @NacosValue(value = "${topic.activate-email.topic-name:topic-picker-email-user-activate-email}")
    private String topicName;
    @NacosValue(value = "${topic.activate-email.topic-partitions:2}")
    private Integer partitions;
    @NacosValue(value = "${topic.activate-email.topic-replication-factor:2}")
    private Short replicationFactor;
}
