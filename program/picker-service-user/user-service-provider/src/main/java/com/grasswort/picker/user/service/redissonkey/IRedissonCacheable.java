package com.grasswort.picker.user.service.redissonkey;

import org.redisson.api.RedissonClient;

/**
 * @author xuliangliang
 * @Classname RedissonKeyGenerator
 * @Description RedissonKeyGenerator
 * @Date 2019/10/9 22:21
 * @blame Java Team
 */
public interface IRedissonCacheable<T> {
    /**
     * 存储
     * @param redissonClient
     * @param value
     */
    void cache(RedissonClient redissonClient, T value);

    /**
     * 获取
     * @param redissonClient
     * @param <T>
     * @return
     */
    T value(RedissonClient redissonClient);
}
