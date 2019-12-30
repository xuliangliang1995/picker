package com.grasswort.picker.blog.service.redisson;

import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author xuliangliang
 * @Classname TopicFavoriteCacheable.java
 * @Description
 * @Date 2019/12/30
 * @blame Java Team
 */
@Component
public class TopicFavoriteCacheable {
    @Resource
    private RedissonClient redissonClient;

    private static final String TOPIC_FAVORITE_CACHE_KEY_TEMPLATE = "pk_user:%s:topic:favorite";

    /**
     * 缓存收藏专题
     * @param userId
     * @param topicIds
     */
    public void cacheUserFavorite(Long userId, List<Long> topicIds) {
        String key = key(userId);
        RList<Long> rTopicIds = redissonClient.getList(key);
        rTopicIds.clear();
        rTopicIds.addAll(topicIds);
    }


    /**
     * 收藏专题列表
     * @param userId
     * @return
     */
    public RList<Long> listFavorite(Long userId) {
        String key = key(userId);
        RList<Long> topicIds = redissonClient.getList(key);
        return topicIds.isExists() ? topicIds : null;
    }

    /**
     * 清除缓存
     * @param userId
     */
    public void clear(Long userId) {
        String key = key(userId);
        RList<Long> topicIds = redissonClient.getList(key);
        topicIds.delete();
    }


    private String key(Long userId) {
        return String.format(TOPIC_FAVORITE_CACHE_KEY_TEMPLATE, userId);
    }
}
