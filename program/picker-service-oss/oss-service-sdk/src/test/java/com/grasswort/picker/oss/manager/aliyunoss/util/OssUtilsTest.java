package com.grasswort.picker.oss.manager.aliyunoss.util;

import com.grasswort.picker.oss.manager.aliyunoss.constant.OssStipulation;

import static org.junit.Assert.*;

/**
 * @author xuliangliang
 * @Classname OssUtilsTest
 * @Description TODO
 * @Date 2019/10/17 21:51
 * @blame Java Team
 */
public class OssUtilsTest {

    @org.junit.Test
    public void generateOssKeyName() {
        String key =  OssUtils.generateOssKeyName();
        assertEquals("OSS key 生成长度有误", OssStipulation.OSS_KEY_NAME_LENGTH, key.length());
    }
}