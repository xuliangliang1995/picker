package com.grasswort.picker.blog.configuration.druid;

import com.alibaba.druid.pool.DruidDataSource;
import com.grasswort.picker.blog.service.task.BlogAutoPushJob;
import com.grasswort.picker.blog.service.task.BlogDocInitJob;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.quartz.QuartzDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

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
    @Primary
    @QuartzDataSource
    @ConfigurationProperties(prefix = "quartz.datasource")
    public DataSource quartzDataSource() {
        return new DruidDataSource();
    }

    @Bean(BlogAutoPushJob.JOB_NAME)
    public JobDetail myJobDetail() {
        return JobBuilder.newJob(BlogAutoPushJob.class)
                .withIdentity(BlogAutoPushJob.JOB_NAME, BlogAutoPushJob.JOB_GROUP)
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger myJobTrigger(@Autowired @Qualifier(BlogAutoPushJob.JOB_NAME) JobDetail jobDetail) {
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity("BLOG_AUTO_PUSH_TRIGGER", "BLOG_TRIGGER_GROUP")
                .withSchedule(
                        CronScheduleBuilder.cronSchedule("0 0 0/1 * * ?")
                ).build();
    }

    @Bean(BlogDocInitJob.JOB_NAME)
    public JobDetail blogDocInitJobDetail() {
        return JobBuilder.newJob(BlogDocInitJob.class)
                .withIdentity(BlogDocInitJob.JOB_NAME, BlogDocInitJob.JOB_GROUP)
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger blogDocInitTrigger(@Autowired @Qualifier(BlogDocInitJob.JOB_NAME) JobDetail jobDetail) {
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity("BLOG_DOC_INIT_TRIGGER", "BLOG_TRIGGER_GROUP")
                .withSchedule(
                        CronScheduleBuilder.cronSchedule("0 0 0/1 * * ?")
                ).build();
    }
}
