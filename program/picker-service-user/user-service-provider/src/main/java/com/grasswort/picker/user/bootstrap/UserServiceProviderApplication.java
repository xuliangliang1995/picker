package com.grasswort.picker.user.bootstrap;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author xuliangliang
 * @Classname UserServiceProviderApplication
 * @Description 启动类
 * @Date 2019/9/22 9:03
 * @blame Java Team
 */
@Slf4j
@SpringBootApplication
@ComponentScans(value = {
        @ComponentScan("com.grasswort.picker.user"),
        @ComponentScan("com.grasswort.picker.commons.config"),
        @ComponentScan("com.grasswort.picker.email.kafka")
})
@MapperScan("com.grasswort.picker.user.dao.persistence")
@EnableElasticsearchRepositories(basePackages = "com.grasswort.picker.user.elastic")
public class UserServiceProviderApplication {

    public static void main(String[] args) {
        System.setProperty("es.set.netty.runtime.available.processors", "false");
        SpringApplication.run(UserServiceProviderApplication.class, args);
    }
}
