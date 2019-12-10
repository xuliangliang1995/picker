package com.grasswort.picker.blog;

import com.grasswort.picker.blog.dto.MyTopicListRequest;
import com.grasswort.picker.blog.dto.MyTopicListResponse;
import com.grasswort.picker.blog.dto.TopicCreateRequest;
import com.grasswort.picker.blog.dto.TopicCreateResponse;

/**
 * @author xuliangliang
 * @Classname IBlogTopicService
 * @Description
 * @Date 2019/12/10 15:34
 * @blame Java Team
 */
public interface IBlogTopicService {

    /**
     * 创建专题
     * @param createRequest
     * @return
     */
    TopicCreateResponse createTopic(TopicCreateRequest createRequest);

    /**
     * 专题列表
     * @param topicListRequest
     * @return
     */
    MyTopicListResponse topics(MyTopicListRequest topicListRequest);
}
