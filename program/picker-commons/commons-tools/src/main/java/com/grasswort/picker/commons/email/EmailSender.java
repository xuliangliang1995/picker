package com.grasswort.picker.commons.email;

/**
 * @author xuliangliang
 * @Classname EmailSender
 * @Description 邮件发送器
 * @Date 2019/10/6 10:11
 * @blame Java Team
 */
public interface EmailSender {
    /**
     * 发送邮件
     * @param body
     * @throws Exception
     */
    void sendMail(MailBody body) throws Exception;

    /**
     * 发送带附件的邮件
     * @param body
     * @throws Exception
     */
    void sendMailWithAttachFile(MailBody body) throws Exception;

    /**
     * 发送 Html 内容的邮件
     * @param body
     * @throws Exception
     */
    void sendHtmlMail(MailBody body) throws Exception;

    /**
     * 使用 HTML 发送邮件
     * @param body
     * @throws Exception
     */
    void sendHtmlMailUseTemplate(MailBody body) throws Exception;
}
