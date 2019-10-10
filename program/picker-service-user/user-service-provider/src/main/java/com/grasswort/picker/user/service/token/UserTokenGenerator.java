package com.grasswort.picker.user.service.token;

import com.grasswort.picker.user.config.lifeline.LifelineConfiguration;
import com.grasswort.picker.user.dao.entity.User;
import com.grasswort.picker.user.dto.JwtAccessTokenUserClaim;
import com.grasswort.picker.user.dto.JwtRefreshTokenUserClaim;
import com.grasswort.picker.user.util.JwtTokenUtil;
import com.grasswort.picker.user.util.MsgPackUtil;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author xuliangliang
 * @Classname UserTokenGenerator
 * @Description token 生成器
 * @Date 2019/10/8 21:51
 * @blame Java Team
 */
@Component
public class UserTokenGenerator {

    @Autowired LifelineConfiguration lifelineConfiguration;

    /**
     * 生成 access_token
     * @param user
     * @return
     */
    public String generateAccessToken(User user) throws IOException {
        JwtAccessTokenUserClaim accessTokenUserClaim = new JwtAccessTokenUserClaim();
        accessTokenUserClaim.setId(user.getId());
        accessTokenUserClaim.setName(user.getName());
        accessTokenUserClaim.setVersion(user.getVersion());

        String accessToken = JwtTokenUtil.creatJwtToken(
                JwtTokenUtil.JwtBody.builder()
                        .msg(MsgPackUtil.write(accessTokenUserClaim))
                        .expiresAt(DateTime.now().plusHours(lifelineConfiguration.getAccessTokenLifeHours()).toDate())
                        .build()
        );
        return accessToken;
    }

    /**
     * 生成 refresh_token
     * @param user
     * @return
     */
    public String generateRefreshToken(User user) throws IOException {
        JwtRefreshTokenUserClaim refreshTokenUserClaim = new JwtRefreshTokenUserClaim();
        refreshTokenUserClaim.setId(user.getId());
        refreshTokenUserClaim.setUsername(user.getUsername());
        refreshTokenUserClaim.setVersion(user.getVersion());

        String refreshToken = JwtTokenUtil.creatJwtToken(
                JwtTokenUtil.JwtBody.builder()
                        .msg(MsgPackUtil.write(refreshTokenUserClaim))
                        .expiresAt(DateTime.now().plusHours(lifelineConfiguration.getRefreshTokenLifeHours()).toDate())
                        .build()
        );
        return refreshToken;
    }
}
