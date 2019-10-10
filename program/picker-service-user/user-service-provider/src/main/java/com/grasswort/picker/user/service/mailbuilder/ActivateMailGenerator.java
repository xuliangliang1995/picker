package com.grasswort.picker.user.service.mailbuilder;

import com.grasswort.picker.commons.email.freemarker.FreeMarkerUtil;
import com.grasswort.picker.email.model.Mail;
import com.grasswort.picker.user.config.lifeline.LifelineConfiguration;
import com.grasswort.picker.user.service.mailbuilder.wrapper.ActivateMailInfoWrapper;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xuliangliang
 * @Classname ActivateMailGenerator
 * @Description 账户激活邮件
 * @Date 2019/10/10 12:39
 * @blame Java Team
 */
@Slf4j
@Component
public class ActivateMailGenerator extends AbstractMailGenerator<ActivateMailInfoWrapper> {

    @Autowired LifelineConfiguration lifelineConfiguration;

    @Override
    public Mail generate(ActivateMailInfoWrapper wrapper) {
        String url = String.format(ACTIVATE_URL_TEMPLATE, wrapper.getUsername(), wrapper.getActivateCode(), wrapper.getActivateId());
        Mail mail = new Mail();
        mail.setSubject(SUBJECT);
        mail.setToAddress(wrapper.getReceivers());
        mail.setCcAddress(Collections.emptyList());
        try {
            Map<String, Object> map = new HashMap<>();
            map.put(Key.TITLE, SUBJECT);
            map.put(Key.URL, url);
            map.put(Key.MINUTES, lifelineConfiguration.getActivationCodeLifeMinutes());
            mail.setContent(FreeMarkerUtil.getMailTextForTemplate(TEMPLATE_PATH, TEMPLATE_NAME, map));
            mail.setHtml(true);
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
            log.info("\n邮件模板解析失败");
            mail.setContent(url);
            mail.setHtml(false);
        }
        return mail;
    }

    private final String ACTIVATE_URL_TEMPLATE = "http://localhost:10001/user/activate?username=%s&code=%s&activateId=%s";

    private final String SUBJECT = "Picker 账户激活";

    private final String TEMPLATE_PATH = "emailTemplate";

    private final String TEMPLATE_NAME = "picker_activate.html";

    private static class Key {
        private static final String TITLE = "title";
        private static final String URL = "url";
        private static final String MINUTES = "minutes";
    }

}
