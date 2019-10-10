package com.grasswort.picker.user.service.mailbuilder;

import com.grasswort.picker.email.model.Mail;
import com.grasswort.picker.user.config.lifeline.LifelineConfiguration;
import com.grasswort.picker.user.service.mailbuilder.wrapper.CaptchaMailInfoWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * @author xuliangliang
 * @Classname CaptchaMailGenerator
 * @Description 验证码邮件生成
 * @Date 2019/10/10 11:59
 * @blame Java Team
 */
@Component
public class CaptchaMailGenerator extends AbstractMailGenerator<CaptchaMailInfoWrapper> {

    @Autowired LifelineConfiguration lifelineConfiguration;

    private final String SUBJECT = "Picker 验证码";

    private final String TEMPLATE = "【验证码】尊敬的 Picker 用户，您的验证码为 %s，该验证码 %d 分钟内有效。如非本人操作，为了您的账户安全，请尽快修改密码。";

    @Override
    public Mail generate(CaptchaMailInfoWrapper wrapper) {
        Mail mail = new Mail();
        mail.setSubject(SUBJECT);
        mail.setToAddress(wrapper.getReceivers());
        mail.setCcAddress(Collections.emptyList());
        mail.setHtml(false);
        String content = String.format(TEMPLATE, wrapper.getCaptcha(), lifelineConfiguration.getCaptchaLifeMinutes());
        mail.setContent(content);
        return mail;
    }




}
