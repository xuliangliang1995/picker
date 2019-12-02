package com.grasswort.picker.elastic.bootstrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 * @author xuliangliang
 * @Classname ElasticBootstrap
 * @Description TODO
 * @Date 2019/11/29 19:27
 * @blame Java Team
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.grasswort.picker.elastic")
@EnableElasticsearchRepositories(basePackages = "com.grasswort.picker.elastic.repository")
public class ElasticBootstrap {

    public static void main(String[] args) {
        SpringApplication.run(ElasticBootstrap.class, args);
    }
}
