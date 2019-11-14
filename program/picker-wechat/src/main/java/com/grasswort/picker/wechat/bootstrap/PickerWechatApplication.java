package com.grasswort.picker.wechat.bootstrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author xuliangliang
 * @Classname PickerWechatApplication
 * @Description 启动类
 * @Date 2019/11/14 15:50
 * @blame Java Team
 */
@ComponentScan(basePackages = "com.grasswort.picker.wechat")
@SpringBootApplication
public class PickerWechatApplication {

    public static void main(String[] args) {
        SpringApplication.run(PickerWechatApplication.class, args);
    }
}
