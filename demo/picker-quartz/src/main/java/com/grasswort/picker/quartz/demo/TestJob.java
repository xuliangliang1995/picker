package com.grasswort.picker.quartz.demo;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author xuliangliang
 * @Classname TestJob
 * @Description 任务测试
 * @Date 2019/11/9 10:41
 * @blame Java Team
 */
public class TestJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        /*String name = jobExecutionContext.getMergedJobDataMap().getString("name");
        System.out.println("执行任务：TestJob " + System.currentTimeMillis());
        System.out.println("name: " + name);*/
        System.out.println("吃饭、睡觉、打豆豆。。。" + System.currentTimeMillis());
    }
}
