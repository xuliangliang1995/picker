package com.grasswort.picker.commons.concurrence;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author xuliangliang
 * @Classname ConcurrenceGun
 * @Description 并发枪（同时开始执行一定数量的任务）
 * @Date 2019/9/21 11:42
 * @blame Java Team
 */
public class ConcurrenceGun {
    /**
     * 并发执行同一个任务
     * @param runnable
     * @param volume
     */
    public static void fire(Runnable runnable, int volume) {
        final CountDownLatch LATCH = new CountDownLatch(volume);
        for (int i = 0; i < volume; i++) {
            Thread thread = new Thread( () -> {
                LATCH.countDown();
                try {
                    LATCH.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runnable.run();
            }, "FIRE_TASK" + (i + 1));
            thread.start();
        }
    }

    /**
     * 并发执行同一任务，直到所有任务执行完毕才返回
     */
    public static void fireButReturnUntilAllTaskDone(Runnable runnable, int volume) {
        final CountDownLatch LATCH = new CountDownLatch(volume);
        final CountDownLatch EXIT = new CountDownLatch(volume);
        for (int i = 0; i < volume; i++) {
            Thread thread = new Thread( () -> {
                try {
                    LATCH.countDown();
                    LATCH.await();
                    runnable.run();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    EXIT.countDown();
                }
            }, "FIRE_TASK" + (i + 1));
            thread.start();
        }
        try {
            EXIT.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试类使用方法，勿用
     * @param runnable
     * @param volume
     */
    public static boolean fireTest(Runnable runnable, int volume) {
        VolumeTestHelper helper = new VolumeTestHelper();
        helper.setVolume(volume);
        final CountDownLatch LATCH = new CountDownLatch(volume);
        final CountDownLatch EXIT = new CountDownLatch(volume);
        for (int i = 0; i < volume; i++) {
            Thread thread = new Thread( () -> {
                LATCH.countDown();
                helper.incr();
                try {
                    LATCH.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                helper.decr();
                runnable.run();
                EXIT.countDown();
            }, "FIRE_TASK" + (i + 1));
            thread.start();
        }
        try {
            EXIT.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return helper.isReachGoal();
    }

    /**
     * 测试类辅助工具
     */
    private static class VolumeTestHelper {
        private int volume;
        private AtomicInteger integer = new AtomicInteger();
        private boolean reachGoal;

        public void incr() {
            int target = integer.incrementAndGet();
            if (target == volume) {
                reachGoal = true;
            }
        }

        public void decr() {
            integer.decrementAndGet();
        }

        public int getVolume() {
            return volume;
        }

        public void setVolume(int volume) {
            this.volume = volume;
        }

        public AtomicInteger getInteger() {
            return integer;
        }

        public void setInteger(AtomicInteger integer) {
            this.integer = integer;
        }

        public boolean isReachGoal() {
            return reachGoal;
        }

        public void setReachGoal(boolean reachGoal) {
            this.reachGoal = reachGoal;
        }
    }
}
