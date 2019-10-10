package com.grasswort.picker.commons.configuration.redis;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * @author xuliangliang
 * @Classname RedissProperties
 * @Description TODO
 * @Date 2019/10/9 21:39
 * @blame Java Team
 */
@ConfigurationProperties(prefix = "spring.redisson", ignoreUnknownFields = false)
@Data
public class RedissonProperties {
    private String address; //连接地址

    private int database;
    /**
     * 等待节点回复命令的时间。该时间从命令发送成功时开始计时
     */
    private int timeout;

    private String password;
    @NestedConfigurationProperty
    private RedissonPoolProperties pool;
}
