package com.grasswort.picker.blog.bootstrap;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author xuliangliang
 * @Classname BlogBootstrap
 * @Description 博客服务启动类
 * @Date 2019/10/19 12:45
 * @blame Java Team
 */
@Slf4j
@SpringBootApplication
@ComponentScans(value = {
        @ComponentScan("com.grasswort.picker.blog"),
        @ComponentScan("com.grasswort.picker.commons.config"),
        @ComponentScan("com.grasswort.picker.email.kafka")
})
@MapperScan("com.grasswort.picker.blog.dao.persistence")
@EnableElasticsearchRepositories(basePackages = "com.grasswort.picker.blog.elastic")
public class BlogProviderBootstrap {

    public static void main(String[] args) {
        System.setProperty("es.set.netty.runtime.available.processors", "false");
        SpringApplication.run(BlogProviderBootstrap.class, args);
    }
}
