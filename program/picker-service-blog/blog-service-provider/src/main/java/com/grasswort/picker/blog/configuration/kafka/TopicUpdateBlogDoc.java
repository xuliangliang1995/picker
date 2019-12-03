package com.grasswort.picker.blog.configuration.kafka;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author xuliangliang
 * @Classname TopicUpdateBlogDoc
 * @Description BlogDoc 更新
 * @Date 2019/12/3 17:19
 * @blame Java Team
 */
@Data
@Component
public class TopicUpdateBlogDoc {
    private String topicName = "topic-picker-elastic-blog-update";

    private Integer partitions = 2;

    private Short replicationFactor = 2;
}
