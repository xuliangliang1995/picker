package com.grasswort.picker.user.service.redissonkey;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/**
 * @author xuliangliang
 * @Classname PkUserVersionCacheKeyGenerator
 * @Description 用户 version 缓存
 * @Date 2019/10/9 22:23
 * @blame Java Team
 */
@Slf4j
@Builder
public class PkUserVersionCacheable implements IRedissonCacheable<Integer> {

    private static final String KEY_TEMPLATE = "pk_user:%s:version";

    public static final long EXPIRE_TIME = 24;

    public static final TimeUnit TIME_UNIT = TimeUnit.HOURS;

    private Long userId;

    /**
     * 存储
     *
     * @param redissonClient
     * @param version
     */
    @Override
    public void cache(RedissonClient redissonClient, Integer version) {
        String key = generateKey();
        RBucket<Integer> rBucket = redissonClient.getBucket(key);
        rBucket.set(version, EXPIRE_TIME, TIME_UNIT);
        log.info("\nRedisson 存储：[{}]：[{}]", key, version);
    }

    /**
     * 获取
     *
     * @param redissonClient
     * @return
     */
    @Override
    public Integer value(RedissonClient redissonClient) {
        String key = generateKey();
        RBucket<Integer> rBucket = redissonClient.getBucket(key);
        Integer version = rBucket.get();
        log.info("\nRedisson 取值：[{}]：[{}]", key, version);
        return version;
    }

    /**
     * 生成 key
     * @return
     */
    private String generateKey() {
        return String.format(KEY_TEMPLATE, userId);
    }
}
