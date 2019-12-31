package com.grasswort.picker.user.dao.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "pk_user_profile")
public class UserProfile {
    @Id
    private Long id;

    @Column(name = "pk_user_id")
    private Long pkUserId;

    /**
     * 个人简介
     */
    private String intro;

    private String github;

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
     * 获取个人简介
     *
     * @return intro - 个人简介
     */
    public String getIntro() {
        return intro;
    }

    /**
     * 设置个人简介
     *
     * @param intro 个人简介
     */
    public void setIntro(String intro) {
        this.intro = intro;
    }

    /**
     * @return github
     */
    public String getGithub() {
        return github;
    }

    /**
     * @param github
     */
    public void setGithub(String github) {
        this.github = github;
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