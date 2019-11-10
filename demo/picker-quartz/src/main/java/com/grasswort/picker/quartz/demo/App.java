package com.grasswort.picker.quartz.demo;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

/**
 * @author xuliangliang
 * @Classname App
 * @Description quartz
 * @Date 2019/11/9 10:38
 * @blame Java Team
 */
public class App {
    public static void main(String[] args) throws SchedulerException {
        // 调度者
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.start();
        // 定义任务
        JobDetail jobDetail = JobBuilder.newJob(TestJob.class)
                .withIdentity("testJob", "test")
                .usingJobData("name", "树林里面丢了鞋")
                .build();
        // 定义触发规则
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("testTrigger", "test")
                .withSchedule(
                        SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(1).repeatForever()
                ).build();

        scheduler.scheduleJob(jobDetail, trigger);
    }
}
