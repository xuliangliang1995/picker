package com.grasswort.picker.blog.service.redisson;

import com.grasswort.picker.blog.dto.topic.TopicMenuItem;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author xuliangliang
 * @Classname TopicMenuCacheable
 * @Description
 * @Date 2019/12/17 14:33
 * @blame Java Team
 */
@Component
public class TopicMenuCacheable {
    @Resource
    private RedissonClient redissonClient;

    private final String TOPIC_MENU_BUCKET = "pk_topic:%s:menu";

    /**
     * 缓存菜单
     * @param topicId
     * @param menus
     */
    public void cacheTopicMenus(Long topicId, List<TopicMenuItem> menus) {
        String key = key(topicId);
        RList<TopicMenuItem> topicMenus = redissonClient.getList(key);
        topicMenus.clear();
        topicMenus.addAll(menus);
    }


    /**
     * 专题菜单
     * @param topicId
     * @return
     */
    public List<TopicMenuItem> topicMenus(Long topicId) {
        String key = key(topicId);
        RList<TopicMenuItem> topicMenus = redissonClient.getList(key);
        return topicMenus.isExists() ? topicMenus.readAll() : null;
    }

    /**
     * 移除菜单
     * @param topicId
     */
    public void removeTopicMenu(Long topicId) {
        String key = key(topicId);
        RList<TopicMenuItem> topicMenus = redissonClient.getList(key);
        topicMenus.delete();
    }

    /**
     * key
     * @param topicId
     * @return
     */
    private String key(Long topicId) {
        return String.format(TOPIC_MENU_BUCKET, topicId);
    }
}
