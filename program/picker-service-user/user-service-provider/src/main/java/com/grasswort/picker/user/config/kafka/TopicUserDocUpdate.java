package com.grasswort.picker.user.config.kafka;

import lombok.Getter;
import org.springframework.stereotype.Component;

/**
 * @author xuliangliang
 * @Classname TopicUserDocUpdate
 * @Description 用户更新主题
 * @Date 2019/12/4 14:19
 * @blame Java Team
 */
@Getter
@Component
public class TopicUserDocUpdate {
    public static final String TOPIC = "topic-picker-elastic-user-update";
    private String topicName = TOPIC;
    private Integer partitions = 2;
    private Short replicationFactor = 2;
}
