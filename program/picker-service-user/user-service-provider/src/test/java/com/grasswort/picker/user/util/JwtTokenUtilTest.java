package com.grasswort.picker.user.util;

import com.grasswort.picker.user.exception.JwtFreeException;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author xuliangliang
 * @Classname JwtTokenUtilsTest
 * @Description 测试 token 生成
 * @Date 2019/10/1 22:46
 * @blame Java Team
 */
public class JwtTokenUtilTest {

    @Test
    public void test() {
        String msg = "grasswort_xuliangliang";
        String token = JwtTokenUtil.creatJwtToken(
                JwtTokenUtil.JwtBody.builder()
                        .msg(msg)
                        .expiresAt(DateTime.now().plusHours(1).toDate())
                        .build()
        );
        String freeMsg = null;
        try {
            freeMsg = JwtTokenUtil.freeJwt(token).getMsg();
        } catch (JwtFreeException e) {
            Assert.assertTrue("JwtToken 解析异常", false);
        }
        Assert.assertEquals("JwtToken 解析异常", msg, freeMsg);
    }

}