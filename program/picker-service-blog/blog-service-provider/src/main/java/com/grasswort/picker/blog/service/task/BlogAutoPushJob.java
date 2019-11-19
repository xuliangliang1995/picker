package com.grasswort.picker.blog.service.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.grasswort.picker.blog.configuration.kafka.TopicBlogPush;
import com.grasswort.picker.blog.constant.BlogCurveStatusEnum;
import com.grasswort.picker.blog.constant.DBGroup;
import com.grasswort.picker.blog.dao.entity.Blog;
import com.grasswort.picker.blog.dao.entity.BlogTrigger;
import com.grasswort.picker.blog.dao.entity.RetentionCurve;
import com.grasswort.picker.blog.dao.persistence.BlogMapper;
import com.grasswort.picker.blog.dao.persistence.BlogTriggerMapper;
import com.grasswort.picker.blog.dao.persistence.RetentionCurveMapper;
import com.grasswort.picker.blog.dto.BlogHtmlRequest;
import com.grasswort.picker.blog.dto.BlogHtmlResponse;
import com.grasswort.picker.blog.service.BlogServiceImpl;
import com.grasswort.picker.blog.service.decorator.BlogUrlDecorator;
import com.grasswort.picker.blog.util.BlogIdEncrypt;
import com.grasswort.picker.commons.config.DBLocalHolder;
import com.grasswort.picker.commons.time.TimeFormat;
import com.grasswort.picker.email.model.Mail;
import com.grasswort.picker.user.IUserSettingService;
import com.grasswort.picker.user.dto.BlogPushSettingRequest;
import com.grasswort.picker.user.dto.BlogPushSettingResponse;
import com.grasswort.picker.wechat.ITemplateMsgService;
import com.grasswort.picker.wechat.dto.WxMpTemplateMsgRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.joda.time.DateTime;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.quartz.QuartzJobBean;
import tk.mybatis.mapper.entity.Example;

import java.util.*;
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

    @Autowired BlogTriggerMapper blogTriggerMapper;

    @Autowired RetentionCurveMapper retentionCurveMapper;

    @Autowired BlogMapper blogMapper;

    @Autowired KafkaTemplate<String, Mail> kafkaTemplate;

    @Autowired TopicBlogPush topicBlogPush;

    @Autowired BlogServiceImpl blogServiceImpl;

    @Reference(version = "1.0", timeout = 10000) IUserSettingService iUserSettingService;

    @Reference(version = "1.0", timeout = 10000) ITemplateMsgService iTemplateMsgService;

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

        Example example = new Example(BlogTrigger.class);
        example.createCriteria()
                .andCondition("datediff(trigger_time, now()) <= 0")
                .andEqualTo("status", BlogCurveStatusEnum.NORMAL.status());
        List<BlogTrigger> blogTriggers = blogTriggerMapper.selectByExample(example);

        log.info("triggers:{}", Optional.ofNullable(blogTriggers).map(List::size).orElse(0));
        for (BlogTrigger trigger: blogTriggers) {
            log.info("trigger:{}", JSON.toJSONString(trigger));
            boolean push = pushBlog(trigger);

            DBLocalHolder.selectDBGroup(DBGroup.MASTER);

            if (push) {
                int newOrder = Math.min(trigger.getRetentionCurveOrder() + 1, MAX_ORDER);
                BlogTrigger triggerSelective = new BlogTrigger();
                triggerSelective.setId(trigger.getId());
                triggerSelective.setRetentionCurveOrder(newOrder);
                triggerSelective.setTriggerTime(DateTime.now().plusDays(ORDER_INTERVAL_DAY_MAP.get(newOrder)).toDate());
                triggerSelective.setGmtModified(DateTime.now().toDate());
                blogTriggerMapper.updateByPrimaryKeySelective(triggerSelective);
            } else {
                BlogTrigger triggerSelective = new BlogTrigger();
                triggerSelective.setId(trigger.getId());
                triggerSelective.setStatus(BlogCurveStatusEnum.STOP.status());
                triggerSelective.setGmtModified(DateTime.now().toDate());
                blogTriggerMapper.updateByPrimaryKeySelective(triggerSelective);
            }
        }

        DBLocalHolder.clear();
        log.info("\n博客推送定时任务执行结束。");
    }

    /**
     * 推送博客
     * @param trigger
     */
    private boolean pushBlog(BlogTrigger trigger) {
        Blog blog = blogMapper.selectByPrimaryKey(trigger.getBlogId());
        BlogPushSettingResponse blogPushSettingResponse = iUserSettingService.getBlogPushSetting(new BlogPushSettingRequest(blog.getPkUserId()));

        if (Optional.ofNullable(blogPushSettingResponse).map(BlogPushSettingResponse::isSuccess).orElse(false)) {
            if (blogPushSettingResponse.getOpenBlogPush()) {
                switch (blogPushSettingResponse.getMode()) {
                    case EMAIL:
                        pushToEmail(blog, blogPushSettingResponse.getEmail());
                        break;
                    case WX:
                        pushToWechat(blog, blogPushSettingResponse.getOpenId());
                        break;
                    default:
                        // ignore
                        break;
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 推送到邮箱
     */
    private void pushToEmail(Blog blog, String email) {
        BlogHtmlResponse htmlResponse = blogServiceImpl.html(new BlogHtmlRequest(BlogIdEncrypt.encrypt(blog.getId())));
        if (Optional.ofNullable(htmlResponse).map(BlogHtmlResponse::isSuccess).orElse(false)) {
            Mail mail = new Mail();
            mail.setSubject(blog.getTitle());
            mail.setHtml(true);
            mail.setContent(htmlResponse.getHtml());
            mail.setToAddress(Collections.singletonList(email));
            mail.setCcAddress(Collections.EMPTY_LIST);
            kafkaTemplate.send(topicBlogPush.getTopicName(), mail);
        }
    }

    /**
     * 推送到微信
     * @param blog
     * @param openId
     */
    private void pushToWechat(Blog blog, String openId) {
        BlogUrlDecorator blogUrlDecorator = new BlogUrlDecorator(blog);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("first", "您有新的内容需要回顾啦 ~ ");
        jsonObject.put("keyword1", blog.getTitle());
        jsonObject.put("keyword2", TimeFormat.format());

        String summary = blog.getSummary();
        if (StringUtils.isBlank(summary)) {
            summary = "--";
        } else if (blog.getSummary().length() > 20) {
            summary = blog.getSummary().substring(0, 20).concat("...");
        }

        jsonObject.put("keyword3", summary);
        jsonObject.put("remark", "感谢您的使用。（点击左下角菜单可以关闭推送或更换推送方式。）");

        iTemplateMsgService.sendTemplateMsg(
                WxMpTemplateMsgRequest.Builder.aWxMpTemplateMsgRequest()
                        .withTemplateId("-nCGb9KHz3gMRWtE3DFh8q88UvrsmiwMES3m1VpR2vY")
                        .withToOpenId(openId)
                        .withUrl(blogUrlDecorator.url())
                        .withJson(JSON.toJSONString(jsonObject))
                        .build()
        );
    }


}
