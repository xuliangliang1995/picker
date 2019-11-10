package com.grasswort.picker.blog.configuration;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author xuliangliang
 * @Classname QuartzDataSourceConfiguration
 * @Description quartzDataSource
 * @Date 2019/11/10 10:52
 * @blame Java Team
 */
@Configuration
public class QuartzDataSourceConfiguration {
    /**
     * quartzDatasource
     * @return
     */
    @Bean("quartzDataSource")
    @ConfigurationProperties(prefix = "quartz.datasource")
    public DataSource quartzDataSource() {
        return new DruidDataSource();
    }
}
