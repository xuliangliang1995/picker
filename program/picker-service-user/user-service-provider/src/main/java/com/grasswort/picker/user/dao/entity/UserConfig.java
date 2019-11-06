package com.grasswort.picker.user.dao.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "pk_user_config")
public class UserConfig {
    @Id
    private Long id;

    @Column(name = "pk_user_id")
    private Long pkUserId;

    @Column(name = "markdown_theme")
    private String markdownTheme;

    /**
     * 安全校验方式 0、邮箱 1、短信
     */
    @Column(name = "safety_check_mode")
    private Integer safetyCheckMode;

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
     * @return markdown_theme
     */
    public String getMarkdownTheme() {
        return markdownTheme;
    }

    /**
     * @param markdownTheme
     */
    public void setMarkdownTheme(String markdownTheme) {
        this.markdownTheme = markdownTheme;
    }

    /**
     * 获取安全校验方式 0、邮箱 1、短信
     *
     * @return safety_check_mode - 安全校验方式 0、邮箱 1、短信
     */
    public Integer getSafetyCheckMode() {
        return safetyCheckMode;
    }

    /**
     * 设置安全校验方式 0、邮箱 1、短信
     *
     * @param safetyCheckMode 安全校验方式 0、邮箱 1、短信
     */
    public void setSafetyCheckMode(Integer safetyCheckMode) {
        this.safetyCheckMode = safetyCheckMode;
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