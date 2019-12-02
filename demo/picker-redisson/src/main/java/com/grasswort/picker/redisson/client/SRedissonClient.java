package com.grasswort.picker.redisson.client;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

/**
 * @author xuliangliang
 * @Classname SRedissonClient
 * @Description 实例
 * @Date 2019/11/29 15:01
 * @blame Java Team
 */
public class SRedissonClient {

    public static RedissonClient client;

    static {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://114.67.99.146:6379")
                .setPassword("Xol4l2y2xx")
                .setDatabase(0)
                .setTimeout(3000)
                .setConnectionMinimumIdleSize(2);
        client = Redisson.create(config);
    }
}
