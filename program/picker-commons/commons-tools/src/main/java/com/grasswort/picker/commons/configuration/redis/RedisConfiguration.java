package com.grasswort.picker.commons.configuration.redis;

import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author xuliangliang
 * @Classname RedisConfiguration
 * @Description Redis配置
 * @Date 2019/10/9 21:38
 * @blame Java Team
 */
@Configuration
@EnableConfigurationProperties(RedissonProperties.class)
@ConditionalOnClass({Redisson.class})
public class RedisConfiguration {
    @Autowired
    RedissonProperties redissonProperties;

    @Bean
    RedissonClient redissonClient() {
        Config config = new Config();
        String[] nodes = redissonProperties.getAddress().split(",");
        for (int i = 0; i < nodes.length; i++) {
            String node = nodes[i];
            nodes[i] = node.startsWith("redis://") ? node : "redis://" + node;
        }
        if (nodes.length == 1) {
            config.useSingleServer()
                    .setAddress(nodes[0])
                    .setTimeout(redissonProperties.getTimeout())
                    .setConnectionMinimumIdleSize(redissonProperties.getPool().getMinIdle())
                    .setPassword(redissonProperties.getPassword());
        } else {
            config.useClusterServers()
                    .addNodeAddress(nodes)
                    .setScanInterval(3000)
                    .setPassword(redissonProperties.getPassword());
        }
        return Redisson.create(config);
    }

    /*public static void main(String[] args) {
        String[] nodes = "114.67.105.79:7001,114.67.105.79:7002,114.67.84.153:7001,114.67.84.153:7002,114.67.104.148:7001,114.67.104.148:7002"
                .split(",");
        for (int i = 0; i < nodes.length; i++) {
            String node = nodes[i];
            nodes[i] = node.startsWith("redis://") ? node : "redis://" + node;
        }
        Config config = new Config();
        config.useClusterServers()
                .addNodeAddress(nodes)
                .setScanInterval(3000)
                .setPassword("xol4l2y2xx");
        RedissonClient client = Redisson.create(config);
        System.out.println(client);


    }*/

}
