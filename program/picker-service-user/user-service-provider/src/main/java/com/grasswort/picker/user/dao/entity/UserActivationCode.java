package com.grasswort.picker.user.dao.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "pk_user_activation_code")
public class UserActivationCode {
    @Id
    private Long id;

    @Column(name = "pk_user_id")
    private Long pkUserId;

    private String username;

    /**
     * 激活码
     */
    @Column(name = "activation_code")
    private String activationCode;

    /**
     * 过期时间
     */
    @Column(name = "expire_time")
    private Date expireTime;

    /**
     * 是否激活 0、否 1、是
     */
    @Column(name = "is_activated")
    private Boolean activated;

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
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 获取激活码
     *
     * @return activation_code - 激活码
     */
    public String getActivationCode() {
        return activationCode;
    }

    /**
     * 设置激活码
     *
     * @param activationCode 激活码
     */
    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }

    /**
     * 获取过期时间
     *
     * @return expire_time - 过期时间
     */
    public Date getExpireTime() {
        return expireTime;
    }

    /**
     * 设置过期时间
     *
     * @param expireTime 过期时间
     */
    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    /**
     * 获取是否激活 0、否 1、是
     *
     * @return is_activated - 是否激活 0、否 1、是
     */
    public Boolean isActivated() {
        return activated;
    }

    /**
     * 设置是否激活 0、否 1、是
     *
     * @param isActivated 是否激活 0、否 1、是
     */
    public void setActivated(Boolean isActivated) {
        this.activated = activated;
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