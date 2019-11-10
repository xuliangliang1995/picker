package com.grasswort.picker.blog.dao.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "pk_blog_trigger")
public class BlogTrigger {
    @Id
    private Long id;

    @Column(name = "blog_id")
    private Long blogId;

    /**
     * 触发时间
     */
    @Column(name = "trigger_time")
    private Date triggerTime;

    private Integer status;

    /**
     * 记忆曲线 Order
     */
    @Column(name = "retention_curve_order")
    private Integer retentionCurveOrder;

    @Column(name = "gmt_create")
    private Date gmtCreate;

    @Column(name = "gmt_modified")
    private Date gmtModified;

    /**
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return blog_id
     */
    public Long getBlogId() {
        return blogId;
    }

    /**
     * @param blogId
     */
    public void setBlogId(Long blogId) {
        this.blogId = blogId;
    }

    /**
     * 获取触发时间
     *
     * @return trigger_time - 触发时间
     */
    public Date getTriggerTime() {
        return triggerTime;
    }

    /**
     * 设置触发时间
     *
     * @param triggerTime 触发时间
     */
    public void setTriggerTime(Date triggerTime) {
        this.triggerTime = triggerTime;
    }

    /**
     * 获取记忆曲线 Order
     *
     * @return retention_curve_order - 记忆曲线 Order
     */
    public Integer getRetentionCurveOrder() {
        return retentionCurveOrder;
    }

    /**
     * 设置记忆曲线 Order
     *
     * @param retentionCurveOrder 记忆曲线 Order
     */
    public void setRetentionCurveOrder(Integer retentionCurveOrder) {
        this.retentionCurveOrder = retentionCurveOrder;
    }

    /**
     * @return gmt_create
     */
    public Date getGmtCreate() {
        return gmtCreate;
    }

    /**
     * @param gmtCreate
     */
    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    /**
     * @return gmt_modified
     */
    public Date getGmtModified() {
        return gmtModified;
    }

    /**
     * @param gmtModified
     */
    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}