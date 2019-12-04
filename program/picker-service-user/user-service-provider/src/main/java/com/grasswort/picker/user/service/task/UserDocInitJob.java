package com.grasswort.picker.user.service.task;

import com.grasswort.picker.user.service.elastic.UserDocInitService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * @author xuliangliang
 * @Classname UserDocInitJob
 * @Description UserDoc es 存储更新
 * @Date 2019/12/4 15:43
 * @blame Java Team
 */
@Slf4j
@DisallowConcurrentExecution
public class UserDocInitJob extends QuartzJobBean {

    public static final String JOB_GROUP = "USER_GROUP";
    public static final String JOB_NAME = "USER_DOC_INIT_JOB";

    @Autowired
    private UserDocInitService userDocInitService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("\n执行用户ES存储更新任务");
        userDocInitService.init();
    }
}
