package com.grasswort.picker.user.dao.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "pk_user")
public class User {
    @Id
    private Long id;

    private String username;

    private String password;

    /**
     * 账号是否激活 0、否 1、是
     */
    @Column(name = "is_activated")
    private Boolean activated;

    private String name;

    /**
     * 只能存储11位的手机号
     */
    private String phone;

    private String email;
    /**
     * 卷耳 Picker 公众号 open_id
     */
    @Column(name = "mp_open_id")
    private String mpOpenId;

    /**
     * 性别 0、未知 1、男 2、女
     */
    private Byte sex;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 版本
     */
    private Integer version;

    /**
     * 创建时间
     */
    @Column(name = "gmt_create")
    private Date gmtCreate;

    /**
     * 更新时间
     */
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
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获取账号是否激活 0、否 1、是
     *
     * @return is_activated - 账号是否激活 0、否 1、是
     */
    public Boolean isActivated() {
        return activated;
    }

    /**
     * 设置账号是否激活 0、否 1、是
     *
     * @param activated 账号是否激活 0、否 1、是
     */
    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取只能存储11位的手机号
     *
     * @return phone - 只能存储11位的手机号
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 设置只能存储11位的手机号
     *
     * @param phone 只能存储11位的手机号
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 获取性别 0、未知 1、男 2、女
     *
     * @return sex - 性别 0、未知 1、男 2、女
     */
    public Byte getSex() {
        return sex;
    }

    /**
     * 设置性别 0、未知 1、男 2、女
     *
     * @param sex 性别 0、未知 1、男 2、女
     */
    public void setSex(Byte sex) {
        this.sex = sex;
    }

    /**
     * 获取创建时间
     *
     * @return gmt_create - 创建时间
     */
    public Date getGmtCreate() {
        return gmtCreate;
    }

    /**
     * 设置创建时间
     *
     * @param gmtCreate 创建时间
     */
    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    /**
     * 获取更新时间
     *
     * @return gmt_modified - 更新时间
     */
    public Date getGmtModified() {
        return gmtModified;
    }

    /**
     * 设置更新时间
     *
     * @param gmtModified 更新时间
     */
    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getMpOpenId() {
        return mpOpenId;
    }

    public void setMpOpenId(String mpOpenId) {
        this.mpOpenId = mpOpenId;
    }
}