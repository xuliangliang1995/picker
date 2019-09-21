package com.grasswort.picker.concurrence;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author xuliangliang
 * @Classname ConcurrenceGunTest
 * @Description TODO
 * @Date 2019/9/21 12:02
 * @blame Java Team
 */
public class ConcurrenceGunTest {

    @Test
    public void testFire() {
        Runnable runnable = () -> {};
        Assert.assertTrue("测试工具【并发枪】有误！", ConcurrenceGun.fireTest(runnable, 1000));
    }

}