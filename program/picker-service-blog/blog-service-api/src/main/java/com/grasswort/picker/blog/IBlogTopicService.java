package com.grasswort.picker.blog;

import com.grasswort.picker.blog.dto.*;

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
     * 编辑专题
     * @param editRequest
     * @return
     */
    TopicEditResponse editTopic(TopicEditRequest editRequest);

    /**
     * 专题列表
     * @param topicListRequest
     * @return
     */
    MyTopicListResponse topics(MyTopicListRequest topicListRequest);

    /**
     * 专题状态更改
     * @param changeRequest
     * @return
     */
    TopicStatusChangeResponse changeStatus(TopicStatusChangeRequest changeRequest);

    /**
     * 删除专题
     * @param deleteRequest
     * @return
     */
    TopicDeleteResponse deleteTopic(TopicDeleteRequest deleteRequest);
}
