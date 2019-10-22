package com.grasswort.picker.blog.bootstrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author xuliangliang
 * @Classname PickerBlogBootStrap
 * @Description 博客启动类
 * @Date 2019/10/21 11:49
 * @blame Java Team
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.grasswort.picker.blog"})
public class PickerBlogBootStrap {

    /**
     * 启动
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(PickerBlogBootStrap.class, args);
    }
}
