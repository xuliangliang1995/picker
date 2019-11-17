package com.grasswort.picker.blog.configuration.kafka;

import com.grasswort.picker.email.constants.EmailCenterConstant;
import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author xuliangliang
 * @Classname TopicBlogPush
 * @Description 博客推送
 * @Date 2019/11/17 15:07
 * @blame Java Team
 */
@Data
@Component
public class TopicBlogPush {

    private String topicName = EmailCenterConstant.TOPIC_EMAIL_PREFIX.concat("blog-push-email");

    private Integer partitions = 2;

    private Short replicationFactor = 2;
}
