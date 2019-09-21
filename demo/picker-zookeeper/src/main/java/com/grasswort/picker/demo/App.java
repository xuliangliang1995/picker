package com.grasswort.picker.demo;

import com.grasswort.picker.concurrence.ConcurrenceGun;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class App {

    private final static String ZOOKEEPER_CLUSTER_URL = "182.92.3.187:2181,182.92.160.62:2181,39.96.42.239:2181";

    private static int i = 0;

    public static void main(String[] args) {
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(ZOOKEEPER_CLUSTER_URL)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();

        curatorFramework.start();
        final String PATH = "/locks/mutex_lock";
        final InterProcessMutex LOCK = new InterProcessMutex(curatorFramework, PATH);

        Runnable runnable = () -> {
            log.info("尝试获取锁！");
            try {
                LOCK.acquire();
                log.info("获取到锁！");
                i ++;
                log.info("do some thing ......");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                log.info("释放锁！");
                try {
                    LOCK.release();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        ConcurrenceGun.fireButReturnUntilAllTaskDone(runnable, 100);
        log.info("当前 i 的值 ：" + i);
    }
    /**
     * InterProcessMutex：分布式可重入排它锁
     * InterProcessSemaphoreMutex：分布式排它锁
     * InterProcessReadWriteLock：分布式读写锁
     */
}
