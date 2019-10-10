package com.grasswort.picker.user.service;

import com.grasswort.picker.commons.annotation.DB;
import com.grasswort.picker.commons.constants.cluster.ClusterFaultMechanism;
import com.grasswort.picker.commons.constants.cluster.ClusterLoadBalance;
import com.grasswort.picker.email.model.Mail;
import com.grasswort.picker.user.ICAPTCHAService;
import com.grasswort.picker.user.config.kafka.TopicActivateEmail;
import com.grasswort.picker.user.config.lifeline.LifelineConfiguration;
import com.grasswort.picker.user.constants.CAPTCHAReceiver;
import com.grasswort.picker.user.constants.DBGroup;
import com.grasswort.picker.user.constants.SysRetCodeConstants;
import com.grasswort.picker.user.dao.entity.Captcha;
import com.grasswort.picker.user.dao.entity.User;
import com.grasswort.picker.user.dao.persistence.CaptchaMapper;
import com.grasswort.picker.user.dao.persistence.UserMapper;
import com.grasswort.picker.user.dto.CAPTCHARequest;
import com.grasswort.picker.user.dto.CAPTCHAResponse;
import com.grasswort.picker.user.service.mailbuilder.CaptchaMailGenerator;
import com.grasswort.picker.user.service.mailbuilder.wrapper.CaptchaMailInfoWrapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Collections;
import java.util.Date;

/**
 * @author xuliangliang
 * @Classname CAPTCHAServiceImpl
 * @Description 验证码服务
 * @Date 2019/10/8 23:11
 * @blame Java Team
 */
@Service(
        version = "1.0",
        timeout = 2000,
        loadbalance = ClusterLoadBalance.LEAST_ACTIVE,
        cluster = ClusterFaultMechanism.FAIL_FAST
)
public class CAPTCHAServiceImpl implements ICAPTCHAService {

    @Autowired UserMapper userMapper;

    @Autowired KafkaTemplate<String, Mail> kafkaTemplate;

    @Autowired CaptchaMapper captchaMapper;

    @Autowired TopicActivateEmail topicActivateEmail;

    @Autowired LifelineConfiguration lifelineConfiguration;

    @Autowired CaptchaMailGenerator captchaMailGenerator;
    /**
     * 验证码长度
     */
    private final int CAPTCHA_CODE_LENGTH = 6;
    /**
     * 发送验证码
     *
     * @param captchaRequest
     * @return
     */
    @Override
    @DB(DBGroup.MASTER)
    public CAPTCHAResponse sendCAPCHA(CAPTCHARequest captchaRequest) {
        CAPTCHAResponse captchaResponse = new CAPTCHAResponse();

        User user = userMapper.selectByPrimaryKey(captchaRequest.getUserId());
        if (user == null) {
            captchaResponse.setCode(SysRetCodeConstants.USER_NOT_EXISTS.getCode());
            captchaResponse.setMsg(SysRetCodeConstants.USER_NOT_EXISTS.getMsg());
            return captchaResponse;
        }
        switch (captchaRequest.getReceiver()) {
            case EMAIL:
                sendEmailCAPCHA(captchaResponse, user);
                break;
            case PHONE:
                sendPhoneCAPCHA(captchaResponse, user);
                break;
            default:
                captchaResponse.setCode(SysRetCodeConstants.SYSTEM_ERROR.getCode());
                captchaResponse.setMsg(SysRetCodeConstants.SYSTEM_ERROR.getMsg());
                break;
        }
        return captchaResponse;
    }

    /**
     * 发送手机验证码
     * @param user
     */
    private void sendPhoneCAPCHA(CAPTCHAResponse response, User user) {
        String phone = user.getPhone();
        if (StringUtils.isNotBlank(phone)) {
            // TODO 暂不处理
            response.setCode(SysRetCodeConstants.SUCCESS.getCode());
            response.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
        } else {
            response.setCode(SysRetCodeConstants.PHONE_IS_NULL.getCode());
            response.setMsg(SysRetCodeConstants.PHONE_IS_NULL.getMsg());
        }
    }

    /**
     * 发送邮箱验证码
     * @param user
     */
    private void sendEmailCAPCHA(CAPTCHAResponse response, User user) {
        String email = user.getEmail();
        if (StringUtils.isBlank(email)) {
            response.setCode(SysRetCodeConstants.EMAIL_IS_NULL.getCode());
            response.setMsg(SysRetCodeConstants.EMAIL_IS_NULL.getMsg());
            return;
        }
        // 生成验证码
        String captchaCode = RandomStringUtils.randomNumeric(CAPTCHA_CODE_LENGTH);

        // 记录到数据库中
        Captcha captcha = new Captcha();
        captcha.setReceiver((byte) CAPTCHAReceiver.EMAIL.getId());
        captcha.setEmail(email);
        captcha.setCaptcha(captchaCode);
        captcha.setExpireTime(DateTime.now().plusMinutes(lifelineConfiguration.getCaptchaLifeMinutes()).toDate());
        Date now = DateTime.now().toDate();
        captcha.setGmtCreate(now);
        captcha.setGmtModified(now);
        captchaMapper.insertUseGeneratedKeys(captcha);

        // 发送验证码邮件
        CaptchaMailInfoWrapper mailInfoWrapper = CaptchaMailInfoWrapper.builder()
                .name(user.getName())
                .captcha(captchaCode)
                .build();
        mailInfoWrapper.setReceivers(Collections.singletonList(user.getEmail()));
        Mail mail = captchaMailGenerator.generate(mailInfoWrapper);
        kafkaTemplate.send(topicActivateEmail.getTopicName(), mail);

        response.setCode(SysRetCodeConstants.SUCCESS.getCode());
        response.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
    }
}
