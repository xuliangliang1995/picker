package com.grasswort.picker.commons.configuration.redis;

import lombok.Data;

/**
 * @author xuliangliang
 * @Classname RedissonPoolProperties
 * @Description TODO
 * @Date 2019/10/9 21:40
 * @blame Java Team
 */
@Data
public class RedissonPoolProperties {
    /**
     * 连接池中的最大空闲连接
     */
    private int maxIdle;
    /**
     * 最小连接数
     */
    private int minIdle;
    /**
     * 连接池最大连接数
     */
    private int maxActive;
    /**
     * 连接池最大阻塞等待时间
     */
    private int maxWait;
}
