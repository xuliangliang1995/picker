package com.grasswort.picker.blog.dao.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "pk_retention_curve")
public class RetentionCurve {
    @Id
    private Long id;

    @Column(name = "curve_order")
    private Integer curveOrder;

    @Column(name = "interval_day")
    private Integer intervalDay;

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

    public Integer getCurveOrder() {
        return curveOrder;
    }

    public void setCurveOrder(Integer curveOrder) {
        this.curveOrder = curveOrder;
    }

    /**
     * @return interval_day
     */
    public Integer getIntervalDay() {
        return intervalDay;
    }

    /**
     * @param intervalDay
     */
    public void setIntervalDay(Integer intervalDay) {
        this.intervalDay = intervalDay;
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
}