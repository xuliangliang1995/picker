package com.grasswort.picker.blog;

import com.grasswort.picker.blog.dto.*;

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

    /**
     * 删除专题菜单
     * @param deleteRequest
     * @return
     */
    DeleteTopicMenuResponse deleteTopicMenu(DeleteTopicMenuRequest deleteRequest);
}
