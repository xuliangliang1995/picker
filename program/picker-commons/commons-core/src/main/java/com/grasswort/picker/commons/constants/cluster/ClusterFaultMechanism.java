package com.grasswort.picker.commons.constants.cluster;

/**
 * @author xuliangliang
 * @Classname ClusterFaultMechanism
 * @Description 集群容错机制
 * @Date 2019/9/24 15:56
 * @blame Java Team
 * 文档：http://dubbo.apache.org/zh-cn/docs/user/demos/fault-tolerent-strategy.html
 */
public class ClusterFaultMechanism {
    /**
     * 失败自动切换。通常用于“读”操作。可通过 retries 设置重试次数
     */
    public static final String FAIL_OVER = "failover";
    /**
     * 快速失败。只发起一次调用，失败立即报错。通常用于非幂等性的“写”操作，比如新增记录。
     */
    public static final String FAIL_FAST = "failfast";
    /**
     * 失败安全。出现异常直接忽略。通常用于写入审计日志等操作
     */
    public static final String FAIL_SAFE = "failsafe";
    /**
     * 失败自动恢复。后台记录失败请求，定时重发。通常用于消息通知操作。（例如支付宝支付结果通知）
     */
    public static final String FAIL_BACK = "failback";
    /**
     * 并行调用多个服务器，只要一个成功即返回。通常用于实时性要求较高的“读”操作。但需要浪费更多服务资源。可通过 forks = 2 来设置最大并行数
     */
    public static final String FORKING = "forking";
    /**
     * 广播调用所有提供者，逐个调用，任意一台报错则报错。通常用于通知所有提供者更新缓存或日志等本地资源信息。
     */
    public static final String BROADCAST = "broadcast";

}
