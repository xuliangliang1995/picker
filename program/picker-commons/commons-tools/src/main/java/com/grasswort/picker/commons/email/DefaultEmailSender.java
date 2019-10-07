package com.grasswort.picker.commons.email;

import com.grasswort.picker.commons.email.freemarker.FreeMarkerUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Vector;

/**
 * @author xuliangliang
 * @Classname DefaultEmailSender
 * @Description 默认邮件发送实现
 * @Date 2019/10/6 10:32
 * @blame Java Team
 */
@Slf4j
@Component
public class DefaultEmailSender implements EmailSender {
    @Autowired
    EmailSmtpConfig emailSmtpConfig;

    @Override
    public void sendMail(MailBody body) throws Exception {
        MimeMessage mimeMessage = initMimeMessage(body);
        mimeMessage.setSubject(body.getSubject());
        mimeMessage.setText(body.getContent());

        Transport.send(mimeMessage);
    }

    @Override
    public void sendMailWithAttachFile(MailBody body) throws Exception {
        MimeMessage mimeMessage = initMimeMessage(body);
        mimeMessage.setSubject(body.getSubject());

        BodyPart textBody = new MimeBodyPart();
        // 消息
        textBody.setText(body.getContent());
        // 创建多重消息
        Multipart multipart = new MimeMultipart();
        // 设置文本消息部分
        multipart.addBodyPart(textBody);

        // 附件部分
        // 设置要发送附件的文件路径
        Vector<String> attachFiles = body.getAttachFileNames();
        attachFiles.forEach(fileName -> {
            try {
                MimeBodyPart attachPart = new MimeBodyPart();
                FileDataSource source = new FileDataSource(fileName);
                attachPart.setDataHandler(new DataHandler(source));
                // 处理附件名称中文（附带文件路径）乱码问题
                attachPart.setFileName(MimeUtility.encodeText(fileName));
                multipart.addBodyPart(attachPart);
            } catch (MessagingException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        });

        // 发送含有附件的完整消息
        mimeMessage.setContent(multipart);
        // 发送附件代码，结束
        Transport.send(mimeMessage);
    }

    @Override
    public void sendHtmlMail(MailBody body) throws Exception {
        MimeMessage mimeMessage = initMimeMessage(body);
        mimeMessage.setSubject(body.getSubject());
        Multipart multipart = new MimeMultipart();
        BodyPart bodyPart = new MimeBodyPart();
        bodyPart.setContent(body.getContent(), body.getContentType());
        multipart.addBodyPart(bodyPart);
        mimeMessage.setContent(multipart);
        Transport.send(mimeMessage);
    }

    @Override
    public void sendHtmlMailUseTemplate(MailBody body) throws Exception {
        MimeMessage mimeMessage = initMimeMessage(body);
        mimeMessage.setSubject(body.getSubject());
        Multipart multipart = new MimeMultipart();
        BodyPart bodyPart = new MimeBodyPart();
        String content = FreeMarkerUtil.getMailTextForTemplate(emailSmtpConfig.getTemplatePath(), body.getTemplateName(), body.getDataMap());
        bodyPart.setContent(content, body.getContentType());
        multipart.addBodyPart(bodyPart);
        mimeMessage.setContent(multipart);
        Transport.send(mimeMessage);
    }

    /**
     * 初始化并完善邮件基本信息
     * @param body
     * @return
     * @throws MessagingException
     */
    private MimeMessage initMimeMessage(MailBody body) throws MessagingException {
        MimeMessage mimeMessage = new MimeMessage(emailSmtpConfig.createMailSession());
        Address fromAddress = emailSmtpConfig.getFromInternetAddress();
        if (fromAddress != null) {
            mimeMessage.setFrom(fromAddress);
        } else {
            mimeMessage.setFrom(emailSmtpConfig.getFromAddress());
        }
        mimeMessage.setRecipients(Message.RecipientType.TO, body.getToInternetAddress());
        // 抄送自己一份，据说可以防止 554
        mimeMessage.addRecipients(Message.RecipientType.CC, emailSmtpConfig.getFromAddress());
        mimeMessage.addRecipients(Message.RecipientType.CC, body.getCcInternetAddress());

        mimeMessage.setSentDate(new Date(System.currentTimeMillis()));
        return mimeMessage;
    }
}
