package com.grasswort.picker.user.dao.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "pk_user_oss_local_ref")
public class UserOssLocalRef {
    @Id
    private Long id;

    /**
     * pk_user_oss_ref 的 id
     */
    @Column(name = "local_ref_id")
    private Long localRefId;

    /**
     * 引用类型，例如：头像
     */
    @Column(name = "ref_type")
    private Byte refType;

    @Column(name = "pk_user_id")
    private Long pkUserId;

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
     * 获取pk_user_oss_ref 的 id
     *
     * @return local_ref_id - pk_user_oss_ref 的 id
     */
    public Long getLocalRefId() {
        return localRefId;
    }

    /**
     * 设置pk_user_oss_ref 的 id
     *
     * @param localRefId pk_user_oss_ref 的 id
     */
    public void setLocalRefId(Long localRefId) {
        this.localRefId = localRefId;
    }

    /**
     * 获取引用类型，例如：头像
     *
     * @return ref_type - 引用类型，例如：头像
     */
    public Byte getRefType() {
        return refType;
    }

    /**
     * 设置引用类型，例如：头像
     *
     * @param refType 引用类型，例如：头像
     */
    public void setRefType(Byte refType) {
        this.refType = refType;
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