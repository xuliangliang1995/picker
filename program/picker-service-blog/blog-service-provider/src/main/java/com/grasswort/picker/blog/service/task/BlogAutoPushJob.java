package com.grasswort.picker.blog.service.task;

import com.grasswort.picker.blog.constant.DBGroup;
import com.grasswort.picker.blog.dao.entity.BlogTrigger;
import com.grasswort.picker.blog.dao.entity.RetentionCurve;
import com.grasswort.picker.blog.dao.persistence.BlogTriggerMapper;
import com.grasswort.picker.blog.dao.persistence.RetentionCurveMapper;
import com.grasswort.picker.blog.dao.persistence.ext.BlogTriggerDao;
import com.grasswort.picker.commons.config.DBLocalHolder;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author xuliangliang
 * @Classname BlogAutoPushJob
 * @Description 博客自动推送服务
 * @Date 2019/11/13 15:31
 * @blame Java Team
 */
@Slf4j
@DisallowConcurrentExecution
public class BlogAutoPushJob extends QuartzJobBean {

    @Autowired
    BlogTriggerDao blogTriggerDao;
    @Autowired
    BlogTriggerMapper blogTriggerMapper;
    @Autowired
    RetentionCurveMapper retentionCurveMapper;

    public static final String JOB_GROUP = "BLOG_GROUP";
    public static final String JOB_NAME = "BLOG_AUTO_PUSH_JOB";

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        DBLocalHolder.selectDBGroup(DBGroup.SLAVE);
        log.info("\n博客推送定时任务执行开始");
        final Map<Integer, Integer> ORDER_INTERVAL_DAY_MAP = retentionCurveMapper.selectAll()
                .stream()
                .collect(Collectors.toMap(RetentionCurve::getCurveOrder, RetentionCurve::getIntervalDay));
        final int MAX_ORDER = ORDER_INTERVAL_DAY_MAP.keySet().stream().max(Comparator.naturalOrder()).get();
        List<BlogTrigger> blogTriggers = blogTriggerDao.listBlogTriggerToday();
        blogTriggers.parallelStream().forEach(trigger -> {
            pushToEmail();
            int newOrder = Math.min(trigger.getRetentionCurveOrder() + 1, MAX_ORDER);

            DBLocalHolder.selectDBGroup(DBGroup.MASTER);
            BlogTrigger triggerSelective = new BlogTrigger();
            triggerSelective.setId(trigger.getId());
            triggerSelective.setRetentionCurveOrder(newOrder);
            triggerSelective.setTriggerTime(DateTime.now().plusDays(ORDER_INTERVAL_DAY_MAP.get(newOrder)).toDate());
            triggerSelective.setGmtModified(DateTime.now().toDate());
            blogTriggerMapper.updateByPrimaryKeySelective(triggerSelective);
        });

        DBLocalHolder.clear();
        log.info("\n博客推送定时任务执行结束。");
    }

    private void pushToEmail() {
    }
}
