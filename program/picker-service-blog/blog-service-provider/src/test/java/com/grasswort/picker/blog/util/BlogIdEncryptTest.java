package com.grasswort.picker.blog.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author xuliangliang
 * @Classname BlogIdEncryptTest
 * @Description 博客ID加解密测试
 * @Date 2019/10/31 17:55
 * @blame Java Team
 */
public class BlogIdEncryptTest {

    @Test
    public void test1() {
        Long blogId = 1L;
        String text = BlogIdEncrypt.encrypt(blogId);
        Assert.assertEquals("博客 ID 加解密有误", blogId, BlogIdEncrypt.decrypt(text));
    }


    @Test
    public void test2() {
        Long blogId = Long.MAX_VALUE;
        String text = BlogIdEncrypt.encrypt(blogId);
        Assert.assertEquals("博客 ID 加解密有误", blogId, BlogIdEncrypt.decrypt(text));
    }

}