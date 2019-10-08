package com.grasswort.picker.user.service;

import com.grasswort.picker.commons.annotation.DB;
import com.grasswort.picker.commons.constants.TOrF;
import com.grasswort.picker.commons.constants.cluster.ClusterFaultMechanism;
import com.grasswort.picker.commons.constants.cluster.ClusterLoadBalance;
import com.grasswort.picker.user.IUserLoginService;
import com.grasswort.picker.user.constants.DBGroup;
import com.grasswort.picker.user.constants.SysRetCodeConstants;
import com.grasswort.picker.user.dao.entity.User;
import com.grasswort.picker.user.dao.persistence.UserMapper;
import com.grasswort.picker.user.dto.*;
import com.grasswort.picker.user.exception.JwtFreeException;
import com.grasswort.picker.user.util.JwtTokenUtil;
import com.grasswort.picker.user.util.MsgPackUtil;
import com.grasswort.picker.user.util.UserTokenGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import tk.mybatis.mapper.entity.Example;

import java.io.IOException;
import java.util.Optional;

/**
 * @author xuliangliang
 * @Classname UserLoginServiceImpl
 * @Description 登录服务
 * @Date 2019/10/2 10:26
 * @blame Java Team
 */
@Slf4j
@Service(
        timeout = 1000,
        loadbalance = ClusterLoadBalance.LEAST_ACTIVE,
        cluster = ClusterFaultMechanism.FAIL_OVER,
        validation = TOrF.FALSE,
        version = "1.0"
)
public class UserLoginServiceImpl implements IUserLoginService {
    @Autowired
    UserMapper userMapper;

    @DB(DBGroup.SLAVE)
    @Override
    public UserLoginResponse login(UserLoginRequest request) {
        log.info("\n用户登录：{}", request.getUsername());
        UserLoginResponse response = new UserLoginResponse();
        String username = request.getUsername();
        String password = request.getPassword();
        Example example = new Example(User.class);
        example.createCriteria().andEqualTo("username", username);
        User user = userMapper.selectOneByExample(example);
        // 账户名或密码错误
        boolean loginFail = ! Optional.ofNullable(user).map(User::getPassword)
                .filter(pwd -> DigestUtils.md5DigestAsHex(password.getBytes()).equals(pwd))
                .isPresent();
        if (loginFail) {
            response.setCode(SysRetCodeConstants.USER_OR_PASSWORD_ERROR.getCode());
            response.setMsg(SysRetCodeConstants.USER_OR_PASSWORD_ERROR.getMsg());
            return response;
        }
        // 账户尚未激活
        boolean isNotActivated = ! user.isActivated();
        if (isNotActivated) {
            response.setCode(SysRetCodeConstants.USER_IS_VERIFIED_ERROR.getCode());
            response.setMsg(SysRetCodeConstants.USER_IS_VERIFIED_ERROR.getMsg());
            return response;
        }
        // 登录成功，生成 JwtToken
        try {
            String accessToken = UserTokenGenerator.generateAccessToken(user);
            String refreshToken = UserTokenGenerator.generateRefreshToken(user);
            response.setCode(SysRetCodeConstants.SUCCESS.getCode());
            response.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
            response.setUsername(user.getUsername());
            response.setPhone(user.getPhone());
            response.setEmail(user.getEmail());
            response.setSex(user.getSex().intValue());
            response.setAccessToken(accessToken);
            response.setRefreshToken(refreshToken);
        } catch (IOException e) {
            // 程序不该到达的地方
            e.printStackTrace();
            response.setCode(SysRetCodeConstants.SYSTEM_ERROR.getCode());
            response.setMsg(SysRetCodeConstants.SYSTEM_ERROR.getMsg());
        }
        return response;
    }

    @Override
    public CheckAuthResponse validToken(CheckAuthRequest request) {
        CheckAuthResponse response = new CheckAuthResponse();
        String token = request.getToken();
        try {
            JwtTokenUtil.JwtBody jwtBody = JwtTokenUtil.freeJwt(token);
            if (jwtBody.getExpiresAt().after(DateTime.now().toDate())) {
                JwtAccessTokenUserClaim userClaim = MsgPackUtil.read(jwtBody.getMsg(), JwtAccessTokenUserClaim.class);
                response.setCode(SysRetCodeConstants.SUCCESS.getCode());
                response.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
                response.setId(userClaim.getId());
                response.setName(userClaim.getName());
                return response;
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.info("\nToken 解析用户信息失败！");
        } catch (JwtFreeException e) {
            // ignore
        }
        response.setCode(SysRetCodeConstants.TOKEN_VALID_FAILED.getCode());
        response.setMsg(SysRetCodeConstants.TOKEN_VALID_FAILED.getMsg());
        return response;
    }
}
