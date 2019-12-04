package com.grasswort.picker.user.config.db;

import com.alibaba.druid.pool.DruidDataSource;
import com.grasswort.picker.user.service.task.UserDocInitJob;
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
 * @Description
 * @Date 2019/12/4 15:41
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

    @Bean(UserDocInitJob.JOB_NAME)
    public JobDetail myJobDetail() {
        return JobBuilder.newJob(UserDocInitJob.class)
                .withIdentity(UserDocInitJob.JOB_NAME, UserDocInitJob.JOB_GROUP)
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger myJobTrigger(@Autowired @Qualifier(UserDocInitJob.JOB_NAME) JobDetail jobDetail) {
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity("USER_DOC_INIT_TRIGGER", "USER_TRIGGER_GROUP")
                .withSchedule(
                        CronScheduleBuilder.cronSchedule("0 0 16 * * ? ")
                ).build();
    }
}
