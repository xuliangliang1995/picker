package com.grasswort.picker.user.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.grasswort.picker.commons.annotation.DB;
import com.grasswort.picker.commons.constants.cluster.ClusterFaultMechanism;
import com.grasswort.picker.commons.constants.cluster.ClusterLoadBalance;
import com.grasswort.picker.commons.mask.MaskUtil;
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
import com.grasswort.picker.user.dto.PhoneCaptchaRequest;
import com.grasswort.picker.user.dto.PhoneCaptchaResponse;
import com.grasswort.picker.user.service.mailbuilder.CaptchaMailGenerator;
import com.grasswort.picker.user.service.mailbuilder.wrapper.CaptchaMailInfoWrapper;
import com.grasswort.picker.user.util.sms.MobileCaptchaSender;
import com.grasswort.picker.wechat.ITemplateMsgService;
import com.grasswort.picker.wechat.dto.WxMpTemplateMsgRequest;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Collections;
import java.util.Date;
import java.util.Optional;

/**
 * @author xuliangliang
 * @Classname CAPTCHAServiceImpl
 * @Description 验证码服务
 * @Date 2019/10/8 23:11
 * @blame Java Team
 */
@Service(
        version = "1.0",
        timeout = 10000,
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

    @Reference(version = "1.0", timeout = 10000) ITemplateMsgService iTemplateMsgService;
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
            case WECHAT:
                sendWechatCAPCHA(captchaResponse, user);
                break;
            default:
                captchaResponse.setCode(SysRetCodeConstants.SYSTEM_ERROR.getCode());
                captchaResponse.setMsg(SysRetCodeConstants.SYSTEM_ERROR.getMsg());
                break;
        }
        return captchaResponse;
    }

    /**
     * 给某个手机号发送验证码
     *
     * @param request
     * @return
     */
    @Override
    @DB(DBGroup.MASTER)
    public PhoneCaptchaResponse sendCaptchaToPhone(PhoneCaptchaRequest request) {
        PhoneCaptchaResponse phoneCaptchaResponse = new PhoneCaptchaResponse();
        String phone = request.getPhone();

        // 生成验证码
        String captchaCode = RandomStringUtils.randomNumeric(CAPTCHA_CODE_LENGTH);

        // 发送验证码短信
        Optional<String> requestIdOpt = MobileCaptchaSender.Builder.aMobileCaptchaSender()
                .withPhone(phone)
                .withCaptcha(captchaCode)
                .build()
                .send();
        if (requestIdOpt.isPresent()) {
            // 记录到数据库中
            Captcha captcha = new Captcha();
            captcha.setReceiver((byte) CAPTCHAReceiver.PHONE.getId());
            captcha.setPhone(phone);
            captcha.setCaptcha(captchaCode);
            captcha.setExpireTime(DateTime.now().plusMinutes(lifelineConfiguration.getCaptchaLifeMinutes()).toDate());
            Date now = DateTime.now().toDate();
            captcha.setGmtCreate(now);
            captcha.setGmtModified(now);
            captchaMapper.insertUseGeneratedKeys(captcha);

            phoneCaptchaResponse.setCode(SysRetCodeConstants.SUCCESS.getCode());
            phoneCaptchaResponse.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
        } else {
            phoneCaptchaResponse.setCode(SysRetCodeConstants.SECURITY_CODE_SEND_FAIL.getCode());
            phoneCaptchaResponse.setMsg(SysRetCodeConstants.SECURITY_CODE_SEND_FAIL.getMsg());
        }

        return phoneCaptchaResponse;
    }

    /**
     * 发送手机验证码
     * @param user
     */
    private void sendPhoneCAPCHA(CAPTCHAResponse response, User user) {
        String phone = user.getPhone();
        if (StringUtils.isBlank(phone)) {
            response.setCode(SysRetCodeConstants.PHONE_IS_NULL.getCode());
            response.setMsg(SysRetCodeConstants.PHONE_IS_NULL.getMsg());
            return;
        }

        // 生成验证码
        String captchaCode = RandomStringUtils.randomNumeric(CAPTCHA_CODE_LENGTH);

        // 发送验证码短信
        Optional<String> requestIdOpt = MobileCaptchaSender.Builder.aMobileCaptchaSender()
                .withPhone(phone)
                .withCaptcha(captchaCode)
                .build()
                .send();
        if (requestIdOpt.isPresent()) {
            // 记录到数据库中
            Captcha captcha = new Captcha();
            captcha.setReceiver((byte) CAPTCHAReceiver.PHONE.getId());
            captcha.setPhone(phone);
            captcha.setCaptcha(captchaCode);
            captcha.setExpireTime(DateTime.now().plusMinutes(lifelineConfiguration.getCaptchaLifeMinutes()).toDate());
            Date now = DateTime.now().toDate();
            captcha.setGmtCreate(now);
            captcha.setGmtModified(now);
            captchaMapper.insertUseGeneratedKeys(captcha);

            response.setPhone(MaskUtil.maskMobile(phone));
            response.setCode(SysRetCodeConstants.SUCCESS.getCode());
            response.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
        } else {
            response.setCode(SysRetCodeConstants.SECURITY_CODE_SEND_FAIL.getCode());
            response.setMsg(SysRetCodeConstants.SECURITY_CODE_SEND_FAIL.getMsg());
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

        response.setEmail(MaskUtil.maskEmail(user.getEmail()));
        response.setCode(SysRetCodeConstants.SUCCESS.getCode());
        response.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
    }

    /**
     * 发送微信验证码（通过公众号）
     * @param response
     * @param user
     */
    private void sendWechatCAPCHA(CAPTCHAResponse response, User user) {
        String openId = user.getMpOpenId();
        if (StringUtils.isBlank(openId)) {
            response.setCode(SysRetCodeConstants.UNBIND_WXMP.getCode());
            response.setMsg(SysRetCodeConstants.UNBIND_WXMP.getMsg());
            return;
        }
        // 生成验证码
        String captchaCode = RandomStringUtils.randomNumeric(CAPTCHA_CODE_LENGTH);

        // 记录到数据库中
        Captcha captcha = new Captcha();
        captcha.setReceiver((byte) CAPTCHAReceiver.WECHAT.getId());
        captcha.setOpenId(openId);
        captcha.setCaptcha(captchaCode);
        captcha.setExpireTime(DateTime.now().plusMinutes(lifelineConfiguration.getCaptchaLifeMinutes()).toDate());
        Date now = DateTime.now().toDate();
        captcha.setGmtCreate(now);
        captcha.setGmtModified(now);
        captchaMapper.insertUseGeneratedKeys(captcha);

        // 发送微信公众号验证码
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("first", "您好，".concat(user.getMpNickName()));
        jsonObject.put("keyword1", captchaCode);
        jsonObject.put("keyword2", lifelineConfiguration.getCaptchaLifeMinutes() + "分钟内有效");
        jsonObject.put("remark", "如非您本人操作，请及时修改密码。");

        WxMpTemplateMsgRequest wxMpTemplateMsgRequest = WxMpTemplateMsgRequest.Builder.aWxMpTemplateMsgRequest()
                .withToOpenId(openId)
                .withTemplateId("a5_yk8Vn5FAXwk9wkBuvoDMaw1IPDnhE1cVbEK7xFSc")
                .withJson(JSON.toJSONString(jsonObject))
                .build();
        iTemplateMsgService.sendTemplateMsg(wxMpTemplateMsgRequest);

        response.setMpNickName(user.getMpNickName());
        response.setCode(SysRetCodeConstants.SUCCESS.getCode());
        response.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
    }
}
