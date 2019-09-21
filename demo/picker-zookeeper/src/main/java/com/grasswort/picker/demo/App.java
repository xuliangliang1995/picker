package com.grasswort.picker.demo;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @author xuliangliang
 * @Classname App
 * @Description TODO
 * @Date 2019/9/21 11:03
 * @blame Java Team
 */
public class App {

    private final static String ZOOKEEPER_CLUSTER_URL = "182.92.3.187:2181,182.92.160.62:2181,39.96.42.239:2181";

    public static void main(String[] args) {
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(ZOOKEEPER_CLUSTER_URL)
                .sessionTimeoutMs(5000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();

        curatorFramework.start();
        final String PATH = "/locks";
        final InterProcessMutex LOCK = new InterProcessMutex(curatorFramework, PATH);

    }
    /**
     * InterProcessMutex：分布式可重入排它锁
     * InterProcessSemaphoreMutex：分布式排它锁
     * InterProcessReadWriteLock：分布式读写锁
     */
}
