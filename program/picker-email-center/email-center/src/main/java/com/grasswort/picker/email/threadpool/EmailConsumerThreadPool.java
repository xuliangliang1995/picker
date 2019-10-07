package com.grasswort.picker.email.threadpool;

import org.apache.commons.lang3.RandomStringUtils;
import sun.plugin2.jvm.RemoteJVMLauncher;

import java.util.concurrent.*;

/**
 * @author xuliangliang
 * @Classname EmailConsumerThreadPool
 * @Description 发送邮件线程池
 * @Date 2019/10/7 11:05
 * @blame Java Team
 */
public class EmailConsumerThreadPool {
    private static final int CORE_POOL_SIZE = 200;
    private static final int MAX_POOL_SIZE = 400;
    private static final String SEND_MAIL_THREAD_PREFIX = "SEND_MAIL_";

    /**
     * 执行任务
     * @param runnable
     */
    public static void execute(Runnable runnable) {
        EXECUTOR_SERVICE.execute(runnable);
    }

    private static final ExecutorService EXECUTOR_SERVICE = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE,
            60L, TimeUnit.SECONDS,
            new SynchronousQueue<Runnable>(), new SendEmailThreadFactory());

    private static class SendEmailThreadFactory implements ThreadFactory {
        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r, SEND_MAIL_THREAD_PREFIX.concat(RandomStringUtils.randomAlphabetic(6)));
            thread.setDaemon(true);
            return thread;
        }
    }
}
