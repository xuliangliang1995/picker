package com.grasswort.picker.commons.email;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * @author xuliangliang
 * @Classname EmailSmtpConfig
 * @Description 邮箱 SMTP 协议配置
 * @Date 2019/10/5 19:44
 * @blame Java Team
 */
@Data
@Component
@ConfigurationProperties(prefix = "email")
public class EmailSmtpConfig {
    /**
     *  邮箱服务器地址
     */
    private String smtpHost;
    /**
     *  IP
     */
    private String smtpPort;
    /**
     * 是否需要授权
     */
    private boolean auth;

    /**
     *  授权信息：用户名
     */
    private String user;
    /**
     *  授权信息：密码
     */
    private String password;
    /**
     *  发信地址
     */
    private String fromAddress;
    /**
     *  发信地址（别名）
     */
    private String fromAlias;
    /**
     * 模板路径
     */
    private String templatePath;

    /**
     * 获取发信地址
     * @return
     */
    public Address getFromInternetAddress() {
        try {
            return new InternetAddress(fromAddress, fromAlias);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 创建邮件会话
     * @return
     */
    public Session createMailSession() {
        // 配置发送邮件的环境属性
        final Properties props = new Properties();
        // 表示SMTP发送邮件，需要进行身份验证
        props.put("mail.smtp.auth", isAuth());
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", smtpPort);
        // 如果使用ssl，则去掉使用25端口的配置，进行如下配置,
        // props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        // props.put("mail.smtp.socketFactory.port", "465");
        // props.put("mail.smtp.port", "465");
        // 发件人的账号，填写控制台配置的发信地址,比如xxx@xxx.com
        props.put("mail.user", user);
        // 访问SMTP服务时需要提供的密码(在控制台选择发信地址进行设置)
        props.put("mail.password", password);
        Session mailSession = Session.getDefaultInstance(props, getAuthenticator());
        return mailSession;
    }

    /**
     * 获取认证器
     * @return
     */
    private Authenticator getAuthenticator() {
        Authenticator authenticator = null;
        if (isAuth()) {
            authenticator = new javax.mail.Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    // 用户名、密码
                    String userName = getUser();
                    String password = getPassword();
                    return new PasswordAuthentication(userName, password);
                }
            };
        }
        return authenticator;
    }




}
