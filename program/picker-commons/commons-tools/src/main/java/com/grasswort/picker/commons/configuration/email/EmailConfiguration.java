package com.grasswort.picker.commons.configuration.email;

import com.grasswort.picker.commons.email.EmailSmtpConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author xuliangliang
 * @Classname EmailConfiguration
 * @Description 邮件配置类
 * @Date 2019/10/6 14:49
 * @blame Java Team
 */
@Configuration
@EnableConfigurationProperties(EmailSmtpConfig.class)
@ComponentScan(value = "com.grasswort.picker.commons.email")
public class EmailConfiguration {
}
