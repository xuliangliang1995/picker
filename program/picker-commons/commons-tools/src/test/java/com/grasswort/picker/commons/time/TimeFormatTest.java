package com.grasswort.picker.commons.time;

import org.junit.Assert;
import org.junit.Test;

import java.time.Instant;

import static org.junit.Assert.*;

/**
 * @author xuliangliang
 * @Classname TimeFormatTest
 * @Description TODO
 * @Date 2019/11/19 17:51
 * @blame Java Team
 */
public class TimeFormatTest {

    @Test
    public void format() {
        System.out.println(TimeFormat.format(TimeFormat.yyyyMMddHHmm));
        assertTrue(true);
    }

    @Test
    public void testFormat() {
        System.out.println(TimeFormat.format(Instant.now(), TimeFormat.yyyyMMddHHmm));
        assertTrue(true);
    }
}