package com.grasswort.picker.blog;

import com.grasswort.picker.blog.dto.TopicPoolRequest;
import com.grasswort.picker.blog.dto.TopicPoolResponse;

/**
 * @author xuliangliang
 * @Classname ITopicPoolService
 * @Description 专题池
 * @Date 2019/12/19 13:50
 * @blame Java Team
 */
public interface ITopicPoolService {

    /**
     * 专题池
     * @param poolRequest
     * @return
     */
    TopicPoolResponse topicPool(TopicPoolRequest poolRequest);

    /**
     * 初始化
     */
    void init();
}
