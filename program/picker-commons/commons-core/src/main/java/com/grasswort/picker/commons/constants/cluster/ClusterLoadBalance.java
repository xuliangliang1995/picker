package com.grasswort.picker.commons.constants.cluster;

/**
 * @author xuliangliang
 * @Classname ClusterLoadBalance
 * @Description 集群负载均衡
 * @Date 2019/9/24 16:08
 * @blame Java Team
 * 文档：http://dubbo.apache.org/zh-cn/docs/user/demos/loadbalance.html
 */
public class ClusterLoadBalance {
    /**
     * 权重随机
     */
    public static final String RANDOM = "random";
    /**
     * 权重轮询
     */
    public static final String ROUND_ROBIN = "roundrobin";
    /**
     * 最少活跃
     */
    public static final String LEAST_ACTIVE = "leastactive";
    /**
     * 一致性 hash
     */
    public static final String CONSISTENT_HASH = "consistenthash";
}
