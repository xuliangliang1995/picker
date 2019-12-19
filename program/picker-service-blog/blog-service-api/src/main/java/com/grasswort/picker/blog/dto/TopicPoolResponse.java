package com.grasswort.picker.blog.dto;

import com.grasswort.picker.blog.dto.topic.TopicItem;
import lombok.Data;

import java.util.List;

/**
 * @author xuliangliang
 * @Classname TopicPoolResponse
 * @Description
 * @Date 2019/12/19 13:52
 * @blame Java Team
 */
@Data
public class TopicPoolResponse extends AbstractBlogResponse {

    private List<TopicItem> topics;

    private long total;

}
