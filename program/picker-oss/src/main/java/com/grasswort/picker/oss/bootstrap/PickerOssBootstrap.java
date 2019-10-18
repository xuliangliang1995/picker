package com.grasswort.picker.oss.bootstrap;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author xuliangliang
 * @Classname PickerOssBootStrap
 * @Description oss 启动类
 * @Date 2019/10/18 17:27
 * @blame Java Team
 */
@EnableSwagger2Doc
@EnableSwagger2
@SpringBootApplication
@ComponentScan(basePackages = {"com.grasswort.picker.oss.controller"})
public class PickerOssBootstrap {

    /**
     * 启动
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(PickerOssBootstrap.class, args);
    }
}
