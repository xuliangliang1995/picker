package com.grasswort.picker.oss.bootstrap;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author xuliangliang
 * @Classname OssProviderBootstrap
 * @Description Oss 服务启动各类
 * @Date 2019/10/17 22:18
 * @blame Java Team
 */
@Slf4j
@SpringBootApplication
@ComponentScans(value = {
        @ComponentScan("com.grasswort.picker.oss"),
        @ComponentScan("com.grasswort.picker.commons.config")
})
@MapperScan("com.grasswort.picker.oss.dao.persistence")
public class OssProviderBootstrap {

    public static void main(String[] args) {
        SpringApplication.run(OssProviderBootstrap.class);
    }
}
