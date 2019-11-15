package com.grasswort.picker.wechat.bootstrap;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author xuliangliang
 * @Classname PickerWechatApplication
 * @Description 启动类
 * @Date 2019/11/14 15:50
 * @blame Java Team
 */
@ComponentScan(basePackages = "com.grasswort.picker.wechat")
@SpringBootApplication
@EnableSwagger2
@EnableSwagger2Doc
public class PickerWechatApplication {

    public static void main(String[] args) {
        SpringApplication.run(PickerWechatApplication.class, args);
    }
}
