package com.grasswort.picker.user.config.kafka;

import com.alibaba.nacos.api.config.ConfigType;
import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import lombok.Getter;
import lombok.ToString;
import org.springframework.stereotype.Component;

/**
 * @author xuliangliang
 * @Classname TopicCaptcha
 * @Description 验证码主题
 * @Date 2019/10/10 14:10
 * @blame Java Team
 */
@Getter
@ToString
@Component
@NacosPropertySource(dataId = "kafka-topics", type = ConfigType.PROPERTIES)
public class TopicCaptcha {
    @NacosValue(value = "${topic.captcha.topic-name:topic-picker-email-user-captcha-email}")
    private String topicName;
    @NacosValue(value = "${topic.captcha.topic-partitions:5}")
    private Integer partitions;
    @NacosValue(value = "${topic.captcha.topic-replication-factor:2}")
    private Short replicationFactor;
}
