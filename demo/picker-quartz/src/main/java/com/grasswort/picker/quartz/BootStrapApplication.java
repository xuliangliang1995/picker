package com.grasswort.picker.quartz;

import com.grasswort.picker.quartz.task.MyJob;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author xuliangliang
 * @Classname SpringBootApplication
 * @Description TODO
 * @Date 2019/11/9 17:40
 * @blame Java Team
 */
@SpringBootApplication
@Configuration
public class BootStrapApplication {

    @Bean("myJob")
    public JobDetail myJobDetail() {
        return JobBuilder.newJob(MyJob.class)
                .withIdentity("myJob", "testGroup")
                .storeDurably()
                .build();
    }

    @Bean("myJobTrigger")
    public Trigger myJobTrigger(@Autowired @Qualifier("myJob") JobDetail jobDetail) {
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity("myJobTrigger", "testTriggerGroup")
                .withSchedule(
                        SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(1).repeatForever()
                ).build();
    }

    public static void main(String[] args) {
        SpringApplication.run(BootStrapApplication.class, args);
    }
}
