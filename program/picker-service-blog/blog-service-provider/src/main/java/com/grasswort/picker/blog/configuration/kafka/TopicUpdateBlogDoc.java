package com.grasswort.picker.blog.configuration.kafka;

import lombok.Getter;
import org.springframework.stereotype.Component;

/**
 * @author xuliangliang
 * @Classname TopicUpdateBlogDoc
 * @Description BlogDoc 更新
 * @Date 2019/12/3 17:19
 * @blame Java Team
 */
@Getter
@Component
public class TopicUpdateBlogDoc {
    public static final String TOPIC = "topic-picker-elastic-blog-update";

    private String topicName = TOPIC;

    private Integer partitions = 2;

    private Short replicationFactor = 2;
}
