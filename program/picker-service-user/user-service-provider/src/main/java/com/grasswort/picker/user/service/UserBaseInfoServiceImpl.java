package com.grasswort.picker.user.service;

import com.grasswort.picker.commons.annotation.DB;
import com.grasswort.picker.commons.constants.TOrF;
import com.grasswort.picker.commons.constants.cluster.ClusterFaultMechanism;
import com.grasswort.picker.commons.constants.cluster.ClusterLoadBalance;
import com.grasswort.picker.user.IUserBaseInfoService;
import com.grasswort.picker.user.constants.DBGroup;
import com.grasswort.picker.user.constants.SysRetCodeConstants;
import com.grasswort.picker.user.dao.entity.User;
import com.grasswort.picker.user.dao.persistence.UserMapper;
import com.grasswort.picker.user.dao.persistence.ext.UserDao;
import com.grasswort.picker.user.dto.*;
import com.grasswort.picker.user.service.redissonkey.PkUserVersionCacheable;
import com.grasswort.picker.user.service.token.UserTokenGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.joda.time.DateTime;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;

import java.io.IOException;

/**
 * @author xuliangliang
 * @Classname UserBaseInfoServiceImpl
 * @Description 用户信息服务
 * @Date 2019/10/9 12:44
 * @blame Java Team
 */
@Slf4j
@Service(
        version = "1.0",
        loadbalance = ClusterLoadBalance.LEAST_ACTIVE,
        cluster = ClusterFaultMechanism.FAIL_OVER,
        validation = TOrF.FALSE
)
public class UserBaseInfoServiceImpl implements IUserBaseInfoService {

    @Autowired UserMapper userMapper;

    @Autowired UserLoginServiceImpl userLoginServiceImpl;

    @Autowired UserDao userDao;

    @Autowired UserTokenGenerator userTokenGenerator;

    @Autowired RedissonClient redissonClient;

    /**
     * 获取用户基本信息
     *
     * @param baseInfoRequest
     * @return
     */
    @Override
    @DB(DBGroup.SLAVE)
    public UserBaseInfoResponse userBaseInfo(UserBaseInfoRequest baseInfoRequest) {
        UserBaseInfoResponse baseInfoResponse = new UserBaseInfoResponse();

        User user = userMapper.selectByPrimaryKey(baseInfoRequest.getUserId());
        if (null == user) {
            baseInfoResponse.setCode(SysRetCodeConstants.USER_NOT_EXISTS.getCode());
            baseInfoResponse.setMsg(SysRetCodeConstants.USER_NOT_EXISTS.getMsg());
            return baseInfoResponse;
        }

        baseInfoResponse.setName(user.getName());
        baseInfoResponse.setSex(user.getSex());
        baseInfoResponse.setEmail(user.getEmail());
        baseInfoResponse.setPhone(user.getPhone());
        baseInfoResponse.setCode(SysRetCodeConstants.SUCCESS.getCode());
        baseInfoResponse.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
        return baseInfoResponse;
    }

    /**
     * 编辑用户基本信息
     *
     * @param editRequest
     * @return
     */
    @Override
    @DB(DBGroup.MASTER)
    public UserBaseInfoEditResponse editUserBaseInfo(UserBaseInfoEditRequest editRequest) {
        UserBaseInfoEditResponse editResponse = new UserBaseInfoEditResponse();

        boolean userExists = userMapper.existsWithPrimaryKey(editRequest.getUserId());
        if (! userExists) {
            editResponse.setCode(SysRetCodeConstants.USER_NOT_EXISTS.getCode());
            editResponse.setMsg(SysRetCodeConstants.USER_NOT_EXISTS.getMsg());
            return editResponse;
        }

        User userSelective = new User();
        userSelective.setId(editRequest.getUserId());
        userSelective.setName(editRequest.getName());
        userSelective.setSex(editRequest.getSex());
        userSelective.setEmail(editRequest.getEmail());
        userSelective.setPhone(editRequest.getPhone());
        userSelective.setGmtModified(DateTime.now().toDate());
        userMapper.updateByPrimaryKeySelective(userSelective);

        User user = userMapper.selectByPrimaryKey(editRequest.getUserId());
        editResponse.setName(user.getName());
        editResponse.setSex(user.getSex());
        editResponse.setPhone(user.getPhone());
        editResponse.setEmail(user.getEmail());
        editResponse.setCode(SysRetCodeConstants.SUCCESS.getCode());
        editResponse.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
        return editResponse;
    }

    /**
     * 用户修改密码
     * 1.修改密码后该用户之前所有 token 都会失效,会返回新的 access_token 和 refresh_token
     * 2.修改密码为敏感操作，需要获取高权限 access_token
     *
     * @param changePwdRequest
     * @return
     */
    @Override
    @DB(DBGroup.MASTER)
    public UserChangePwdResponse changePwd(UserChangePwdRequest changePwdRequest) {
        UserChangePwdResponse changePwdResponse = new UserChangePwdResponse();
        // 敏感操作，二次校验身份
        CheckAuthRequest checkAuthRequest = new CheckAuthRequest();
        checkAuthRequest.setToken(changePwdRequest.getAccessToken());
        checkAuthRequest.setIp(changePwdRequest.getIp());
        CheckAuthResponse authResponse = userLoginServiceImpl.validToken(checkAuthRequest);

        boolean authSuccess = SysRetCodeConstants.SUCCESS.getCode().equals(authResponse.getCode());

        if (! authSuccess) {
            log.info("\n修改密码身份校验失败");
            changePwdResponse.setMsg(SysRetCodeConstants.TOKEN_VALID_FAILED.getMsg());
            changePwdResponse.setCode(SysRetCodeConstants.TOKEN_VALID_FAILED.getCode());
            return changePwdResponse;
        }

        boolean permissionDenied = ! authResponse.isPrivilege();
        if (permissionDenied) {
            log.info("\n修改密码权限不足。用户ID：{}", authResponse.getId());
            changePwdResponse.setMsg(SysRetCodeConstants.PERMISSION_DENIED.getMsg());
            changePwdResponse.setCode(SysRetCodeConstants.PERMISSION_DENIED.getCode());
            return changePwdResponse;
        }

        if (authSuccess) {
            Long userId = authResponse.getId();
            User user = userMapper.selectByPrimaryKey(userId);
            log.info("\n修改密码。用户ID：{}", userId);
            final int VERSION_OLD = user.getVersion();
            final int VERSION_NEW = VERSION_OLD + 1;
            String encryptPassword = DigestUtils.md5DigestAsHex(changePwdRequest.getPassword().getBytes());
            int result = userDao.updatePassword(userId, encryptPassword, VERSION_OLD, VERSION_NEW);
            boolean updateSuccess = result > 0;
            if (updateSuccess) {
                try {
                    user.setVersion(VERSION_NEW);
                    // 修改成功，重新给客户端生成 token, accessToken 使用高级 token
                    String privilegeAccessToken = userTokenGenerator.generatePrivilegeAccessToken(user, changePwdRequest.getIp());
                    String refreshToken = userTokenGenerator.generateRefreshToken(user);
                    changePwdResponse.setAccessToken(privilegeAccessToken);
                    changePwdResponse.setRefreshToken(refreshToken);
                } catch (IOException e) {
                    log.info("\nToken 生成失败：{}", e.getMessage());
                    e.printStackTrace();
                }
                // 更新 redis 缓存， 之前该用户所有颁发的 token 都会失效
                PkUserVersionCacheable.builder().userId(userId).build().cache(redissonClient, VERSION_NEW);

                changePwdResponse.setCode(SysRetCodeConstants.SUCCESS.getCode());
                changePwdResponse.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
                log.info("\n修改密码成功。用户ID：{}", userId);
                return changePwdResponse;
            } else {
                changePwdResponse.setCode(SysRetCodeConstants.PLEASE_RETRY.getCode());
                changePwdResponse.setMsg(SysRetCodeConstants.PLEASE_RETRY.getMsg());
                log.info("\n修改密码数据库操作失败，建议重试。用户ID：{}", userId);
                return changePwdResponse;
            }
        }
        changePwdResponse.setCode(SysRetCodeConstants.SYSTEM_ERROR.getCode());
        changePwdResponse.setMsg(SysRetCodeConstants.SYSTEM_ERROR.getMsg());
        log.info("\n修改密码。系统错误。{}", changePwdRequest);
        return changePwdResponse;
    }
}
