package com.grasswort.picker.blog.service.task;

import com.grasswort.picker.blog.dao.persistence.BlogMapper;
import com.grasswort.picker.blog.elastic.repository.BlogDocRepository;
import com.grasswort.picker.blog.service.elastic.BlogDocConverter;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;

/**
 * @author xuliangliang
 * @Classname BlogDocInitJob
 * @Description 博客 elasticsearch 初始化
 * @Date 2019/12/2 16:45
 * @blame Java Team
 */
@Slf4j
@DisallowConcurrentExecution
public class BlogDocInitJob extends QuartzJobBean {

    public static final String JOB_GROUP = "BLOG_GROUP";
    public static final String JOB_NAME = "BLOG_DOC_INIT_JOB";

    @Resource BlogMapper blogMapper;

    @Resource BlogDocRepository blogDocRepository;

    @Resource BlogDocConverter blogDocConverter;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("\n执行博客ES存储更新任务");
        blogMapper.selectAll().forEach(blog -> blogDocRepository.save(blogDocConverter.blog2BlogDoc(blog)));
    }
}
