package com.grasswort.picker.email.bootstrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

/**
 * @author xuliangliang
 * @Classname EmailCenterBootStrap
 * @Description 邮件中心启动器
 * @Date 2019/10/6 15:12
 * @blame Java Team
 */
@SpringBootApplication
@ComponentScans(value = {
        @ComponentScan("com.grasswort.picker.commons.configuration.email"),
        @ComponentScan("com.grasswort.picker.email.kafka")
})
public class EmailCenterBootStrap {
    /**
     * 邮件中心只（发送不带附件的邮件）
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(EmailCenterBootStrap.class);
    }
}
