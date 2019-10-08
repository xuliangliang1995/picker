package com.grasswort.picker.user.service;

import com.grasswort.picker.commons.annotation.DB;
import com.grasswort.picker.commons.constants.cluster.ClusterFaultMechanism;
import com.grasswort.picker.commons.constants.cluster.ClusterLoadBalance;
import com.grasswort.picker.user.IUserTokenRefreshService;
import com.grasswort.picker.user.constants.DBGroup;
import com.grasswort.picker.user.constants.SysRetCodeConstants;
import com.grasswort.picker.user.dao.entity.User;
import com.grasswort.picker.user.dao.persistence.UserMapper;
import com.grasswort.picker.user.dto.JwtRefreshTokenUserClaim;
import com.grasswort.picker.user.dto.RefreshAccessTokenRequest;
import com.grasswort.picker.user.dto.RefreshAccessTokenResponse;
import com.grasswort.picker.user.exception.JwtFreeException;
import com.grasswort.picker.user.util.JwtTokenUtil;
import com.grasswort.picker.user.util.MsgPackUtil;
import com.grasswort.picker.user.util.UserTokenGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Objects;

/**
 * @author xuliangliang
 * @Classname UserTokenRefreshServiceImpl
 * @Description refresh accessToken
 * @Date 2019/10/8 21:38
 * @blame Java Team
 */
@Slf4j
@Service(
        version = "1.0",
        loadbalance = ClusterLoadBalance.LEAST_ACTIVE,
        cluster = ClusterFaultMechanism.FAIL_OVER
)
public class UserTokenRefreshServiceImpl implements IUserTokenRefreshService {
    @Autowired
    UserMapper userMapper;
    /**
     * refresh accessToken
     *
     * @param refreshAccessTokenRequest
     * @return
     */
    @Override
    @DB(DBGroup.SLAVE)
    public RefreshAccessTokenResponse refreshAccessToken(RefreshAccessTokenRequest refreshAccessTokenRequest) {
        RefreshAccessTokenResponse refreshResponse = new RefreshAccessTokenResponse();
        try {
            JwtTokenUtil.JwtBody jwtBody = JwtTokenUtil.freeJwt(refreshAccessTokenRequest.getRefreshToken());
            boolean isEffective = jwtBody.getExpiresAt().after(DateTime.now().toDate()) && StringUtils.isNotBlank(jwtBody.getMsg());
            if (isEffective) {
                JwtRefreshTokenUserClaim claim = MsgPackUtil.read(jwtBody.getMsg(), JwtRefreshTokenUserClaim.class);
                User user = userMapper.selectByPrimaryKey(claim.getId());
                isEffective = Objects.equals(user.getVersion(), claim.getVersion());
                if (isEffective) {
                    String accessToken = UserTokenGenerator.generateAccessToken(user);
                    String refreshToken = UserTokenGenerator.generateRefreshToken(user);
                    refreshResponse.setAccessToken(accessToken);
                    refreshResponse.setRefreshToken(refreshToken);
                    refreshResponse.setCode(SysRetCodeConstants.SUCCESS.getCode());
                    refreshResponse.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
                    log.info("\naccessToken 刷新成功");
                    return refreshResponse;
                }
            }
        } catch (JwtFreeException | IOException e) {
            log.info("\naccessToken 刷新失败。{}", refreshAccessTokenRequest.getRefreshToken());
            e.printStackTrace();
        }
        refreshResponse.setCode(SysRetCodeConstants.REFRESH_TOKEN_VALID_FAILED.getCode());
        refreshResponse.setMsg(SysRetCodeConstants.REFRESH_TOKEN_VALID_FAILED.getMsg());
        return refreshResponse;
    }
}
