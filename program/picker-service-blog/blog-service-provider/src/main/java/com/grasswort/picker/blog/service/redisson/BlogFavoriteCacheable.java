package com.grasswort.picker.blog.service.redisson;

import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author xuliangliang
 * @Classname BlogFavoriteCacheable.java
 * @Description
 * @Date 2019/12/30
 * @blame Java Team
 */
@Component
public class BlogFavoriteCacheable {
    @Resource
    private RedissonClient redissonClient;

    private static final String BLOG_FAVORITE_CACHE_KEY_TEMPLATE = "pk_user:%s:blog:favorite";

    /**
     * 缓存收藏博客
     * @param userId
     * @param blogIds
     */
    public void cacheUserFavorite(Long userId, List<Long> blogIds) {
        String key = key(userId);
        RList<Long> rBlogIds = redissonClient.getList(key);
        rBlogIds.clear();
        rBlogIds.addAll(blogIds);
    }


    /**
     * 收藏博客列表
     * @param userId
     * @return
     */
    public RList<Long> listFavorite(Long userId) {
        String key = key(userId);
        RList<Long> blogIds = redissonClient.getList(key);
        return blogIds.isExists() ? blogIds : null;
    }

    /**
     * 清除缓存
     * @param userId
     */
    public void clear(Long userId) {
        String key = key(userId);
        RList<Long> blogIds = redissonClient.getList(key);
        blogIds.delete();
    }


    private String key(Long userId) {
        return String.format(BLOG_FAVORITE_CACHE_KEY_TEMPLATE, userId);
    }
}
