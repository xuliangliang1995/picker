package com.grasswort.picker.blog.service;

import com.grasswort.picker.blog.IRetentionCurveService;
import com.grasswort.picker.blog.constant.BlogCurveStatusEnum;
import com.grasswort.picker.blog.constant.DBGroup;
import com.grasswort.picker.blog.constant.SysRetCodeConstants;
import com.grasswort.picker.blog.dao.entity.BlogTrigger;
import com.grasswort.picker.blog.dao.persistence.BlogTriggerMapper;
import com.grasswort.picker.blog.dao.persistence.RetentionCurveMapper;
import com.grasswort.picker.blog.dao.persistence.ext.BlogTriggerDao;
import com.grasswort.picker.blog.dao.persistence.ext.RetentionCurveDao;
import com.grasswort.picker.blog.dto.BlogCurveRequest;
import com.grasswort.picker.blog.dto.BlogCurveResponse;
import com.grasswort.picker.blog.dto.CurveListResponse;
import com.grasswort.picker.blog.dto.curve.RetentionCurveItem;
import com.grasswort.picker.blog.util.BlogIdEncrypt;
import com.grasswort.picker.commons.annotation.DB;
import org.apache.dubbo.config.annotation.Service;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author xuliangliang
 * @Classname RetentionCurveServiceImpl
 * @Description 记忆曲线
 * @Date 2019/11/10 12:11
 * @blame Java Team
 */
@Service(version = "1.0", timeout = 10000)
public class RetentionCurveServiceImpl implements IRetentionCurveService {

    @Autowired RetentionCurveMapper retentionCurveMapper;

    @Autowired BlogTriggerMapper blogTriggerMapper;

    @Autowired BlogTriggerDao blogTriggerDao;

    @Autowired RetentionCurveDao retentionCurveDao;

    /**
     * 获取记忆曲线
     * @return
     */
    @Override
    @DB(DBGroup.SLAVE)
    public CurveListResponse retentionCurve() {
        CurveListResponse curveListResponse = new CurveListResponse();

        List<RetentionCurveItem> curves = retentionCurveMapper.selectAll()
                .stream().map(curve -> RetentionCurveItem.Builder.aRetentionCurveItem()
                        .withCurveId(curve.getId())
                        .withOrder(curve.getCurveOrder())
                        .withIntervalDay(curve.getIntervalDay())
                        .build())
                .collect(Collectors.toList());

        curveListResponse.setCurves(curves);
        curveListResponse.setCode(SysRetCodeConstants.SUCCESS.getCode());
        curveListResponse.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
        return curveListResponse;
    }

    /**
     * 博客记忆曲线修改
     *
     * @param blogCurveRequest
     * @return
     */
    @Override
    @DB(DBGroup.MASTER)
    public BlogCurveResponse blogCurvePatch(BlogCurveRequest blogCurveRequest) {
        BlogCurveResponse blogCurveResponse = new BlogCurveResponse();

        BlogIdEncrypt.BlogKey blogKey = BlogIdEncrypt.decrypt(blogCurveRequest.getBlogId());
        Long blogId = Optional.ofNullable(blogKey).map(BlogIdEncrypt.BlogKey::getBlogId).orElse(null);
        if (blogId == null) {
            blogCurveResponse.setCode(SysRetCodeConstants.BLOG_NOT_EXISTS.getCode());
            blogCurveResponse.setMsg(SysRetCodeConstants.BLOG_NOT_EXISTS.getMsg());
            return blogCurveResponse;
        }

        Integer order = blogCurveRequest.getOrder();
        Integer intervalDay = null;
        if (order != null && order > 0) {
            intervalDay = retentionCurveDao.selectIntervalDayByOrder(order);
            if (intervalDay == null) {
                blogCurveResponse.setCode(SysRetCodeConstants.CURVE_NOT_EXISTS.getCode());
                blogCurveResponse.setMsg(SysRetCodeConstants.CURVE_NOT_EXISTS.getMsg());
                return blogCurveResponse;
            }
        }

        BlogCurveStatusEnum statusEnum = blogCurveRequest.getStatus();

        BlogTrigger blogTrigger = blogTriggerDao.selectOneByBlogId(blogId);
        Date now = DateTime.now().toDate();
        if (blogTrigger == null) {
            // 新增
            BlogTrigger trigger = new BlogTrigger();
            trigger.setBlogId(blogId);

            if (order != null && order > 0) {
                trigger.setRetentionCurveOrder(order);
                trigger.setTriggerTime(DateTime.now().plusDays(intervalDay).toDate());
            } else {
                trigger.setRetentionCurveOrder(1);
                trigger.setTriggerTime(DateTime.now().plusDays(retentionCurveDao.selectIntervalDayByOrder(1)).toDate());
            }

            trigger.setStatus(statusEnum == null ? BlogCurveStatusEnum.NORMAL.status() : statusEnum.status());
            trigger.setGmtCreate(now);
            trigger.setGmtModified(now);
            blogTriggerMapper.insert(trigger);
        } else {
            // 修改
            boolean orderChange = order != null && order > 0 && Objects.equals(order, blogTrigger.getRetentionCurveOrder());
            if (orderChange) {
                Integer oldIntervalDay = retentionCurveDao.selectIntervalDayByOrder(blogTrigger.getRetentionCurveOrder());
                Integer newIntervalDay = retentionCurveDao.selectIntervalDayByOrder(order);
                Date newTriggerTime = new Date(blogTrigger.getTriggerTime().toInstant().plus((newIntervalDay - oldIntervalDay), ChronoUnit.DAYS).toEpochMilli());
                blogTrigger.setTriggerTime(newTriggerTime.before(now) ? now : newTriggerTime);
                blogTrigger.setRetentionCurveOrder(order);
            }
            if (statusEnum != null) {
                blogTrigger.setStatus(statusEnum.status());
            }
            blogTrigger.setGmtModified(now);
            blogTriggerMapper.updateByPrimaryKey(blogTrigger);
        }

        blogCurveResponse.setCode(SysRetCodeConstants.SUCCESS.getCode());
        blogCurveResponse.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
        return blogCurveResponse;
    }
}
