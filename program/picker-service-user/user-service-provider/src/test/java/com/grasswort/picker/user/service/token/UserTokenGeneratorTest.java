package com.grasswort.picker.user.service.token;

import com.grasswort.picker.user.config.lifeline.LifelineConfiguration;
import com.grasswort.picker.user.dao.entity.User;
import com.grasswort.picker.user.dto.JwtAccessTokenUserClaim;
import com.grasswort.picker.user.exception.JwtFreeException;
import com.grasswort.picker.user.util.JwtTokenUtil;
import com.grasswort.picker.user.util.MsgPackUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * @author xuliangliang
 * @Classname UserTokenGeneratorTest
 * @Description UserTokenGeneratorTest
 * @Date 2019/10/12 17:49
 * @blame Java Team
 */
public class UserTokenGeneratorTest {

    private User user;

    private String ip;

    private UserTokenGenerator userTokenGenerator;

    @Before
    public void setUp() throws Exception {
        userTokenGenerator = new UserTokenGenerator();

        LifelineConfiguration lifelineConfiguration = new LifelineConfiguration();
        lifelineConfiguration.setAccessTokenLifeHours(1);
        lifelineConfiguration.setRefreshTokenLifeHours(1);
        lifelineConfiguration.setTokenPrivilegeLifeMinutes(1);
        userTokenGenerator.setLifelineConfiguration(lifelineConfiguration);

        user = new User();
        user.setId(1L);
        user.setName("xuliangliang");
        user.setVersion(1);

        ip = "127.0.0.1";
    }

    @After
    public void tearDown() throws Exception {
        user = null;
        ip = null;
        userTokenGenerator = null;
    }

    @Test
    public void generateAccessToken() {
        String accessToken = null;
        try {
            accessToken = userTokenGenerator.generateAccessToken(user);
        } catch (IOException e) {
            e.printStackTrace();
            assertFalse("access_token 生成异常",true);
        }
        JwtTokenUtil.JwtBody jwtBody = null;
        try {
            jwtBody = JwtTokenUtil.freeJwt(accessToken);
        } catch (JwtFreeException e) {
            e.printStackTrace();
            assertFalse("access_token Jwt解析异常", true);
        }
        JwtAccessTokenUserClaim userClaim = null;
        try {
            userClaim = MsgPackUtil.read(jwtBody.getMsg(), JwtAccessTokenUserClaim.class);
        } catch (IOException e) {
            e.printStackTrace();
            assertFalse("access_token MsgPack反序列化异常", true);
        }

        assertEquals("access_token 解析后 ID 有误", user.getId(), userClaim.getId());
        assertEquals("access_token 解析后 用户名 有误", user.getName(), userClaim.getName());
        assertEquals("access_token 解析后 用户 version 有误", user.getVersion(), userClaim.getVersion());
    }

    @Test
    public void generatePrivilegeAccessToken() {
        String accessToken = null;
        try {
            accessToken = userTokenGenerator.generatePrivilegeAccessToken(user, ip);
        } catch (IOException e) {
            e.printStackTrace();
            assertFalse("privilege_access_token 生成异常",true);
        }
        JwtTokenUtil.JwtBody jwtBody = null;
        try {
            jwtBody = JwtTokenUtil.freeJwt(accessToken);
        } catch (JwtFreeException e) {
            e.printStackTrace();
            assertFalse("privilege_access_token Jwt解析异常", true);
        }
        JwtAccessTokenUserClaim userClaim = null;
        try {
            userClaim = MsgPackUtil.read(jwtBody.getMsg(), JwtAccessTokenUserClaim.class);
        } catch (IOException e) {
            e.printStackTrace();
            assertFalse("privilege_access_token MsgPack反序列化异常", true);
        }

        assertEquals("privilege_access_token 解析后 ID 有误", user.getId(), userClaim.getId());
        assertEquals("privilege_access_token 解析后 用户名 有误", user.getName(), userClaim.getName());
        assertEquals("privilege_access_token 解析后 用户 version 有误", user.getVersion(), userClaim.getVersion());
        assertEquals("privilege_access_token 解析后 IP 有误", ip, userClaim.getIp());
    }

    @Test
    public void generateRefreshToken() {
    }
}