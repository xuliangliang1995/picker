package com.grasswort.picker.quartz.task;

import com.grasswort.picker.quartz.service.TestService;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * @author xuliangliang
 * @Classname MyJob
 * @Description MyJob
 * @Date 2019/11/9 17:43
 * @blame Java Team
 */
@DisallowConcurrentExecution
public class MyJob extends QuartzJobBean {
    @Autowired
    TestService testService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        testService.doSomeThing();
    }
}
