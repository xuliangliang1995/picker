package com.grasswort.picker.blog.dto;

import com.grasswort.picker.blog.dto.topic.TopicCommentItem;
import lombok.Data;

import java.util.List;

/**
 * @author xuliangliang
 * @Classname TopicCommentsResponse
 * @Description
 * @Date 2019/12/23 16:04
 * @blame Java Team
 */
@Data
public class TopicCommentsResponse extends AbstractBlogResponse {

    private List<TopicCommentItem> comments;

    private Long total;
    /**
     * 是否已经评价
     */
    private Boolean hasComment;

}
