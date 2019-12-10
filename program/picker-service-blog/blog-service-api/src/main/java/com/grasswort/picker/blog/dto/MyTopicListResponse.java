package com.grasswort.picker.blog.dto;

import com.grasswort.picker.blog.dto.topic.TopicItem;
import lombok.Data;

import java.util.List;

/**
 * @author xuliangliang
 * @Classname MyTopicListResponse
 * @Description
 * @Date 2019/12/10 15:50
 * @blame Java Team
 */
@Data
public class MyTopicListResponse extends AbstractBlogResponse {

    private List<TopicItem> topics;

    private Long total;
}
