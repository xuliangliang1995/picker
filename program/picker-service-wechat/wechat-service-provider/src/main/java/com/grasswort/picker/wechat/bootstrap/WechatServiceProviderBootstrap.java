package com.grasswort.picker.wechat.bootstrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author xuliangliang
 * @Classname WechatServiceProviderBootstrap
 * @Description 启动类
 * @Date 2019/11/14 15:14
 * @blame Java Team
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.grasswort.picker.wechat")
public class WechatServiceProviderBootstrap {

    public static void main(String[] args) {
        SpringApplication.run(WechatServiceProviderBootstrap.class, args);
    }
}
