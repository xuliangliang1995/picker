package com.grasswort.picker.blog;

import com.grasswort.picker.blog.dto.TopicMenuCreateRequest;
import com.grasswort.picker.blog.dto.TopicMenuCreateResponse;
import com.grasswort.picker.blog.dto.TopicMenuRequest;
import com.grasswort.picker.blog.dto.TopicMenuResponse;

/**
 * @author xuliangliang
 * @Classname IBlogTopicMenuService
 * @Description 专题菜单
 * @Date 2019/12/11 13:58
 * @blame Java Team
 */
public interface IBlogTopicMenuService {

    /**
     * 菜单创建
     * @param menuCreateRequest
     * @return
     */
    TopicMenuCreateResponse createMenu(TopicMenuCreateRequest menuCreateRequest);

    /**
     * 专题菜单
     * @param topicMenuRequest
     * @return
     */
    TopicMenuResponse topicMenu(TopicMenuRequest topicMenuRequest);
}
