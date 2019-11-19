package com.grasswort.picker.user.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.grasswort.picker.commons.annotation.DB;
import com.grasswort.picker.commons.config.DBLocalHolder;
import com.grasswort.picker.commons.constants.TOrF;
import com.grasswort.picker.commons.constants.cluster.ClusterFaultMechanism;
import com.grasswort.picker.commons.constants.cluster.ClusterLoadBalance;
import com.grasswort.picker.commons.mask.MaskUtil;
import com.grasswort.picker.commons.time.TimeFormat;
import com.grasswort.picker.oss.IOssRefService;
import com.grasswort.picker.oss.constants.OssConstants;
import com.grasswort.picker.oss.dto.OssRefRequest;
import com.grasswort.picker.oss.dto.OssRefResponse;
import com.grasswort.picker.oss.manager.aliyunoss.dto.OssRefDTO;
import com.grasswort.picker.oss.manager.aliyunoss.util.OssUtils;
import com.grasswort.picker.user.IUserBaseInfoService;
import com.grasswort.picker.user.constants.DBGroup;
import com.grasswort.picker.user.constants.SysRetCodeConstants;
import com.grasswort.picker.user.dao.entity.Captcha;
import com.grasswort.picker.user.dao.entity.User;
import com.grasswort.picker.user.dao.entity.UserOssRef;
import com.grasswort.picker.user.dao.persistence.CaptchaMapper;
import com.grasswort.picker.user.dao.persistence.UserMapper;
import com.grasswort.picker.user.dao.persistence.UserOssRefMapper;
import com.grasswort.picker.user.dao.persistence.ext.UserDao;
import com.grasswort.picker.user.dto.*;
import com.grasswort.picker.user.service.redissonkey.PkUserVersionCacheable;
import com.grasswort.picker.user.service.token.UserTokenGenerator;
import com.grasswort.picker.wechat.ITemplateMsgService;
import com.grasswort.picker.wechat.IWxMpUserInfoService;
import com.grasswort.picker.wechat.dto.WxMpTemplateMsgRequest;
import com.grasswort.picker.wechat.dto.WxMpUserInfoRequest;
import com.grasswort.picker.wechat.dto.WxMpUserInfoResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.joda.time.DateTime;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;
import tk.mybatis.mapper.entity.Example;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

    @Autowired UserOssRefMapper userOssRefMapper;

    @Autowired CaptchaMapper captchaMapper;

    @Reference(version = "1.0", timeout = 10000) IWxMpUserInfoService iWxMpUserInfoService;

    @Reference(version = "1.0", timeout = 10000) IOssRefService iOssRefService;

    @Reference(version = "1.0", timeout = 10000) ITemplateMsgService iTemplateMsgService;

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
        baseInfoResponse.setEmail(MaskUtil.maskEmail(user.getEmail()));
        baseInfoResponse.setPhone(MaskUtil.maskMobile(user.getPhone()));
        baseInfoResponse.setAvatar(user.getAvatar());
        baseInfoResponse.setMpNickName(user.getMpNickName());
        baseInfoResponse.setMpHeadImgUrl(user.getMpHeadImgUrl());
        baseInfoResponse.setBindWechat(StringUtils.isNotBlank(user.getMpOpenId()));
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
        userSelective.setAvatar(editRequest.getAvatar());
        userSelective.setGmtModified(DateTime.now().toDate());
        userMapper.updateByPrimaryKeySelective(userSelective);

        processAvatarRef(editRequest.getAvatar());

        User user = userMapper.selectByPrimaryKey(editRequest.getUserId());
        editResponse.setName(user.getName());
        editResponse.setSex(user.getSex());
        editResponse.setPhone(user.getPhone());
        editResponse.setEmail(user.getEmail());
        editResponse.setAvatar(user.getAvatar());
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

        boolean authSuccess = Optional.ofNullable(authResponse).map(CheckAuthResponse::isSuccess).orElse(false);

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

    /**
     * 更换手机号（绑定手机号）
     *
     * @param changePhoneRequest
     * @return
     */
    @Override
    @DB(DBGroup.MASTER)
    public UserChangePhoneResponse changePhone(UserChangePhoneRequest changePhoneRequest) {
        UserChangePhoneResponse changePhoneResponse = new UserChangePhoneResponse();
        // 敏感操作，二次校验身份
        CheckAuthRequest checkAuthRequest = new CheckAuthRequest();
        checkAuthRequest.setToken(changePhoneRequest.getAccessToken());
        checkAuthRequest.setIp(changePhoneRequest.getIp());
        CheckAuthResponse authResponse = userLoginServiceImpl.validToken(checkAuthRequest);

        boolean authSuccess = Optional.ofNullable(authResponse).map(CheckAuthResponse::isSuccess).orElse(false);

        if (! authSuccess) {
            log.info("\n修改手机号身份校验失败");
            changePhoneResponse.setMsg(SysRetCodeConstants.TOKEN_VALID_FAILED.getMsg());
            changePhoneResponse.setCode(SysRetCodeConstants.TOKEN_VALID_FAILED.getCode());
            return changePhoneResponse;
        }

        boolean permissionDenied = ! authResponse.isPrivilege();
        if (permissionDenied) {
            log.info("\n修改手机号权限不足。用户ID：{}", authResponse.getId());
            changePhoneResponse.setMsg(SysRetCodeConstants.PERMISSION_DENIED.getMsg());
            changePhoneResponse.setCode(SysRetCodeConstants.PERMISSION_DENIED.getCode());
            return changePhoneResponse;
        }

        if (authSuccess) {
            Long userId = authResponse.getId();
            User user = userMapper.selectByPrimaryKey(userId);
            log.info("\n修改手机号。用户ID：{}", userId);
            final int VERSION_OLD = user.getVersion();
            // 1. 先判断手机号是否有效
            String phone = changePhoneRequest.getPhone();
            String captcha = changePhoneRequest.getCaptcha();
            Example example = new Example(Captcha.class);
            example.createCriteria().andEqualTo("captcha", captcha).andEqualTo("phone", phone)
                    .andGreaterThan("expireTime", DateTime.now().toDate());
            List<Captcha> captchas = captchaMapper.selectByExample(example);
            boolean phoneIsValid = (! CollectionUtils.isEmpty(captchas)) && captchas.stream().findFirst().isPresent();
            if (phoneIsValid) {
                // 开始修改手机号
                User userSelective = new User();
                userSelective.setId(userId);
                userSelective.setPhone(phone);
                userSelective.setGmtModified(DateTime.now().toDate());
                userMapper.updateByPrimaryKeySelective(userSelective);
                log.info("\n修改手机号成功。用户ID：{}", userId);
                changePhoneResponse.setCode(SysRetCodeConstants.SUCCESS.getCode());
                changePhoneResponse.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
                return changePhoneResponse;
            }

        }
        changePhoneResponse.setCode(SysRetCodeConstants.SYSTEM_ERROR.getCode());
        changePhoneResponse.setMsg(SysRetCodeConstants.SYSTEM_ERROR.getMsg());
        log.info("\n修改手机号。系统错误。{}", changePhoneRequest);
        return changePhoneResponse;
    }

    /**
     * 更换绑定公众号用户
     *
     * @param changeMpOpenIdRequest
     * @return
     */
    @Override
    @DB(DBGroup.SLAVE)
    public ChangeMpOpenIdResponse changeOrBindMpOpenId(ChangeMpOpenIdRequest changeMpOpenIdRequest) {
        ChangeMpOpenIdResponse response = new ChangeMpOpenIdResponse();

        Long userId = changeMpOpenIdRequest.getUserId();
        String openId = changeMpOpenIdRequest.getOpenId();

        // 不做非空判断。自认为空的走不到这一步
        User user = userMapper.selectByPrimaryKey(userId);
        String oldOpenId = user.getMpOpenId();

        if (! Objects.equals(openId, oldOpenId) || StringUtils.isAnyBlank(user.getMpNickName(), user.getMpHeadImgUrl())) {
            DBLocalHolder.selectDBGroup(DBGroup.MASTER);

            User userSelective = new User();
            userSelective.setId(userId);
            userSelective.setMpOpenId(openId);
            userSelective.setGmtModified(DateTime.now().toDate());

            WxMpUserInfoRequest userInfoRequest = new WxMpUserInfoRequest();
            userInfoRequest.setOpenId(openId);
            WxMpUserInfoResponse userInfoResponse = iWxMpUserInfoService.wxMpUserInfo(userInfoRequest);
            if (Optional.ofNullable(userInfoResponse).map(WxMpUserInfoResponse::isSuccess).orElse(false)) {
                userSelective.setMpHeadImgUrl(userInfoResponse.getHeadImgUrl());
                userSelective.setMpNickName(userInfoResponse.getNickName());
            }

            userMapper.updateByPrimaryKeySelective(userSelective);
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("first", "您好，您的账号已成功绑定此微信。");
        jsonObject.put("keyword1", MaskUtil.maskEmail(user.getEmail()));
        jsonObject.put("keyword2", TimeFormat.format());
        jsonObject.put("remark", "感谢您的使用。");

        iTemplateMsgService.sendTemplateMsg(
                WxMpTemplateMsgRequest.Builder.aWxMpTemplateMsgRequest()
                        .withTemplateId("KHMQ17xWd1cDIDBZ2G4p1AhCu-ee6zxo2drDwJSEiQs")
                        .withJson(JSON.toJSONString(jsonObject))
                        .withToOpenId(openId)
                        .build()
        );

        response.setCode(SysRetCodeConstants.SUCCESS.getCode());
        response.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
        return response;
    }


    /**
     * 处理头像 oss 引用
     * @param avatar
     */
    private void processAvatarRef(String avatar) {
        OssRefDTO ref = OssUtils.resolverUrl(avatar);
        if (ref != null) {
            // 1、判断是否建议远程应用
            Example example = new Example(UserOssRef.class);
            example.createCriteria().andEqualTo("ossKey", ref.getObjectKey())
                    .andEqualTo("ossBucket", ref.getBucketName());

            int count = userOssRefMapper.selectCountByExample(example);
            if (count == 0) {
                final String DEL_KEY = RandomStringUtils.randomAlphabetic(OssConstants.OSS_DEL_KEY_LENGTH);
                OssRefResponse refResponse = iOssRefService.recordRef(
                        OssRefRequest.Builder.anOssRefRequest()
                        .withObjectKey(ref.getObjectKey())
                        .withBucketName(ref.getBucketName())
                        .withDelKey(DEL_KEY)
                        .build()
                );

                final long OSS_REF_ID = Optional.ofNullable(refResponse)
                        .map(r -> r.isSuccess() ? r.getId() : 0L)
                        .orElse(0L);
                Date now = DateTime.now().toDate();
                UserOssRef remoteRef = new UserOssRef();
                remoteRef.setOssKey(ref.getObjectKey());
                remoteRef.setOssBucket(ref.getBucketName());
                remoteRef.setOssRefId(OSS_REF_ID);
                remoteRef.setOssRefDelKey(DEL_KEY);
                remoteRef.setGmtCreate(now);
                remoteRef.setGmtModified(now);
                userOssRefMapper.insert(remoteRef);
            }
        }
    }
}
