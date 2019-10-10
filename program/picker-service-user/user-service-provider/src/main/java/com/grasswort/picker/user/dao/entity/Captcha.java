package com.grasswort.picker.user.dao.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "pk_captcha")
public class Captcha {
    @Id
    private Long id;

    /**
     * 验证码接收者类型
     */
    private Byte receiver;

    private String phone;

    private String email;

    private String captcha;

    @Column(name = "expire_time")
    private Date expireTime;

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
     * 获取验证码接收者类型
     *
     * @return receiver - 验证码接收者类型
     */
    public Byte getReceiver() {
        return receiver;
    }

    /**
     * 设置验证码接收者类型
     *
     * @param receiver 验证码接收者类型
     */
    public void setReceiver(Byte receiver) {
        this.receiver = receiver;
    }

    /**
     * @return phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone
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
     * @return captcha
     */
    public String getCaptcha() {
        return captcha;
    }

    /**
     * @param captcha
     */
    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    /**
     * @return expire_time
     */
    public Date getExpireTime() {
        return expireTime;
    }

    /**
     * @param expireTime
     */
    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
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