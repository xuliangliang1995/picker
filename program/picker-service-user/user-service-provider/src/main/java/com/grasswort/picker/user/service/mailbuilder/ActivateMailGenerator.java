package com.grasswort.picker.user.service.mailbuilder;

import com.grasswort.picker.email.model.Mail;
import com.grasswort.picker.user.config.lifeline.LifelineConfiguration;
import com.grasswort.picker.user.service.mailbuilder.wrapper.ActivateMailInfoWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;

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

        StringBuilder contentBuilder = new StringBuilder();
        contentBuilder.append("<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta charset=\"utf-8\" />\n" +
                "</head>\n" +
                "<title>");
        contentBuilder.append(SUBJECT);
        contentBuilder.append("</title>\n" +
                "<body style=\"height: 100%\">\n" +
                "    <div style=\"width: 100%;height: 300px;background-color: whitesmoke;border-color: gainsboro\">\n" +
                "        <div>\n" +
                "            <P><strong>Picker 账户激活邮件</strong></P>\n" +
                "        </div>\n" +
                "        <div>\n" +
                "            <p>尊敬的用户，您好。</p>\n" +
                "            <p>欢迎成为新的 <strong>Picker</strong> 注册用户。</p>\n" +
                "            <p>请点击以下链接完成账户的激活（");

        contentBuilder.append(lifelineConfiguration.getActivationCodeLifeMinutes());
        contentBuilder.append("分钟内有效）。如非本人操作，请忽略。</p>\n" +
                "            <p><a href='");
        contentBuilder.append(url);
        contentBuilder.append("'>点击激活");
        contentBuilder.append("</a></p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "\n" +
                "</body>\n" +
                "</html>");
        mail.setContent(contentBuilder.toString());
        mail.setHtml(true);
        return mail;
    }

    private final String ACTIVATE_URL_TEMPLATE = "https://grasswort.com/api/user/activate?username=%s&code=%s&activateId=%s";

    private final String SUBJECT = "Picker 账户激活";

}
