package com.grasswort.picker.blog;

import com.grasswort.picker.blog.dto.TopicCommentRequest;
import com.grasswort.picker.blog.dto.TopicCommentResponse;
import com.grasswort.picker.blog.dto.TopicCommentsRequest;
import com.grasswort.picker.blog.dto.TopicCommentsResponse;

/**
 * @author xuliangliang
 * @Classname ITopicCommentService
 * @Description
 * @Date 2019/12/23 15:30
 * @blame Java Team
 */
public interface ITopicCommentService {

    /**
     * 评分
     * @param commentRequest
     * @return
     */
    TopicCommentResponse topicComment(TopicCommentRequest commentRequest);

    /**
     * 评分列表
     * @param commentsRequest
     * @return
     */
    TopicCommentsResponse topicComments(TopicCommentsRequest commentsRequest);



}
