package com.grasswort.picker.user.dao.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "pk_user_subscribe_log")
public class UserSubscribeLog {
    @Id
    private Long id;

    @Column(name = "pk_user_id")
    private Long pkUserId;

    @Column(name = "author_id")
    private Long authorId;

    /**
     * 1、关注 2、取消关注
     */
    private Integer behavior;

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
     * @return pk_user_id
     */
    public Long getPkUserId() {
        return pkUserId;
    }

    /**
     * @param pkUserId
     */
    public void setPkUserId(Long pkUserId) {
        this.pkUserId = pkUserId;
    }

    /**
     * @return author_id
     */
    public Long getAuthorId() {
        return authorId;
    }

    /**
     * @param authorId
     */
    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    /**
     * 获取1、关注 2、取消关注
     *
     * @return behavior - 1、关注 2、取消关注
     */
    public Integer getBehavior() {
        return behavior;
    }

    /**
     * 设置1、关注 2、取消关注
     *
     * @param behavior 1、关注 2、取消关注
     */
    public void setBehavior(Integer behavior) {
        this.behavior = behavior;
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