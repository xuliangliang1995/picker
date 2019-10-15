package com.grasswort.picker.user.service;

import com.grasswort.picker.commons.annotation.DB;
import com.grasswort.picker.commons.config.DBLocalHolder;
import com.grasswort.picker.commons.constants.TOrF;
import com.grasswort.picker.commons.constants.cluster.ClusterFaultMechanism;
import com.grasswort.picker.commons.constants.cluster.ClusterLoadBalance;
import com.grasswort.picker.email.model.Mail;
import com.grasswort.picker.user.IUserActivateService;
import com.grasswort.picker.user.config.kafka.TopicCaptcha;
import com.grasswort.picker.user.constants.DBGroup;
import com.grasswort.picker.user.constants.SysRetCodeConstants;
import com.grasswort.picker.user.dao.entity.User;
import com.grasswort.picker.user.dao.entity.UserActivationCode;
import com.grasswort.picker.user.dao.persistence.UserActivationCodeMapper;
import com.grasswort.picker.user.dao.persistence.UserMapper;
import com.grasswort.picker.user.dto.*;
import com.grasswort.picker.user.service.mailbuilder.ActivateMailGenerator;
import com.grasswort.picker.user.service.mailbuilder.wrapper.ActivateMailInfoWrapper;
import com.grasswort.picker.user.service.redissonkey.PkUserVersionCacheable;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.joda.time.DateTime;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Collections;
import java.util.Date;

/**
 * @author xuliangliang
 * @Classname UserActivateServiceImpl
 * @Description 用户激活服务
 * @Date 2019/10/6 22:40
 * @blame Java Team
 */
@Slf4j
@Service(
        version = "1.0",
        timeout = 2000,
        loadbalance = ClusterLoadBalance.LEAST_ACTIVE,
        cluster = ClusterFaultMechanism.FAIL_SAFE,
        validation = TOrF.FALSE
)
public class UserActivateServiceImpl implements IUserActivateService {

    @Autowired UserActivationCodeMapper userActivationCodeMapper;

    @Autowired UserMapper userMapper;

    @Autowired KafkaTemplate<String, Mail> kafkaTemplate;

    @Autowired RedissonClient redissonClient;

    @Autowired TopicCaptcha topicCaptcha;

    @Autowired ActivateMailGenerator activateMailGenerator;
    /**
     * 账号激活链接有效分钟数
     */
    final int ACTIVATED_URL_EFFECTIVE_MINUTES = 10;
    /**
     * 激活码长度（固定 32）
     */
    final int ACTIVATED_CODE_LENGTH = 32;

    /**
     * 查看用户激活状态
     *
     * @param request
     * @return
     */
    @Override
    @DB(DBGroup.SLAVE)
    public QueryActivateStatusResponse activateStatus(QueryActivateStatusRequest request) {
        QueryActivateStatusResponse activateStatusResponse = new QueryActivateStatusResponse();

        String username = request.getUsername();
        Example example = new Example(User.class);
        example.createCriteria().andEqualTo("username", username);
        User user = userMapper.selectOneByExample(example);

        if (user == null) {
            activateStatusResponse.setCode(SysRetCodeConstants.USER_NOT_EXISTS.getCode());
            activateStatusResponse.setMsg(SysRetCodeConstants.USER_NOT_EXISTS.getMsg());
            return activateStatusResponse;
        }

        boolean isActivated = user.isActivated();
        activateStatusResponse.setCode(SysRetCodeConstants.SUCCESS.getCode());
        activateStatusResponse.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
        activateStatusResponse.setActivated(isActivated);
        if (! isActivated) {
            activateStatusResponse.setEmail(user.getEmail());
        }
        return activateStatusResponse;
    }

    /**
     * 发送账户激活邮件
     * @param request
     * @return
     */
    @Override
    @DB(DBGroup.SLAVE)
    public SendActivateEmailResponse sendActivateEmail(SendActivateEmailRequest request) {
        SendActivateEmailResponse response = new SendActivateEmailResponse();

        Example example = new Example(User.class);
        example.createCriteria().andEqualTo("username", request.getUsername());
        User user = userMapper.selectOneByExample(example);

        if (user == null) {
            response.setCode(SysRetCodeConstants.USERNAME_NOT_EXISTS.getCode());
            response.setMsg(SysRetCodeConstants.USERNAME_NOT_EXISTS.getMsg());
            return response;
        }

        if (user.isActivated()) {
            response.setCode(SysRetCodeConstants.USERNAME_IS_ACTIVATED.getCode());
            response.setMsg(SysRetCodeConstants.USERNAME_IS_ACTIVATED.getMsg());
            return response;
        }

        this.sendActivateEmail(user.getId());
        response.setEmail(user.getEmail());
        response.setCode(SysRetCodeConstants.SUCCESS.getCode());
        response.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
        return response;
    }

    /**
     * 注册用户激活
     *
     * @param request
     * @return
     */
    @Override
    @DB(DBGroup.SLAVE)
    public UserActivateResponse activate(UserActivateRequest request) {
        UserActivateResponse response = new UserActivateResponse();
        // 判断是否有效
        Long activateId = request.getActivateId();
        UserActivationCode activationCode = userActivationCodeMapper.selectByPrimaryKey(activateId);
        boolean efficacious = activationCode != null && activationCode.getUsername().equals(request.getUsername())
                && activationCode.getActivationCode().equals(request.getActivationCode()) && activationCode.getExpireTime().after(DateTime.now().toDate());

        boolean loseEfficacy = ! efficacious;
        if (loseEfficacy) {
            // 链接已失效
            response.setCode(SysRetCodeConstants.ACTIVATE_URL_LOSE_EFFICACY.getCode());
            response.setMsg(SysRetCodeConstants.ACTIVATE_URL_LOSE_EFFICACY.getMsg());
            return response;
        }

        boolean isNotActivated = ! activationCode.isActivated();
        if (isNotActivated) {
            // 账号尚未激活
            DBLocalHolder.selectDBGroup(DBGroup.MASTER);
            this.executeActivate(activationCode);
        }
        // 激活成功
        response.setCode(SysRetCodeConstants.SUCCESS.getCode());
        response.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
        return response;

    }

    /**
     * 执行真正的激活
     * @param activationCode
     */
    @Transactional(rollbackFor = Exception.class)
    @DB(DBGroup.MASTER)
    public void executeActivate(UserActivationCode activationCode) {
        activationCode.setActivated(true);
        activationCode.setGmtModified(DateTime.now().toDate());
        userActivationCodeMapper.updateByPrimaryKey(activationCode);

        User user = new User();
        user.setId(activationCode.getPkUserId());
        user.setActivated(true);
        user.setGmtModified(DateTime.now().toDate());
        userMapper.updateByPrimaryKeySelective(user);
        // 存储用户版本
        PkUserVersionCacheable.builder().userId(user.getId()).build().cache(redissonClient, 1);
    }

    /**
     * 发送激活邮件（允许极小几率发送失败）
     * @param userId
     */
    @DB(DBGroup.MASTER)
    public void sendActivateEmail(long userId) {
        DBLocalHolder.selectDBGroup(DBGroup.MASTER);
        User user = userMapper.selectByPrimaryKey(userId);
        // 生成激活链接
        Date now = DateTime.now().toDate();
        UserActivationCode activationCode = new UserActivationCode();
        activationCode.setPkUserId(user.getId());
        activationCode.setUsername(user.getUsername());
        activationCode.setActivated(false);
        activationCode.setExpireTime(DateTime.now().plusMinutes(ACTIVATED_URL_EFFECTIVE_MINUTES).toDate());
        activationCode.setGmtCreate(now);
        activationCode.setGmtModified(now);
        activationCode.setActivationCode(RandomStringUtils.randomAlphabetic(ACTIVATED_CODE_LENGTH));
        userActivationCodeMapper.insertUseGeneratedKeys(activationCode);

        ActivateMailInfoWrapper mailInfoWrapper = ActivateMailInfoWrapper.builder()
                .activateId(activationCode.getId())
                .username(activationCode.getUsername())
                .activateCode(activationCode.getActivationCode())
                .build();
        mailInfoWrapper.setReceivers(Collections.singletonList(user.getEmail()));
        Mail mail = activateMailGenerator.generate(mailInfoWrapper);
        kafkaTemplate.send(topicCaptcha.getTopicName(), mail);
    }
}
