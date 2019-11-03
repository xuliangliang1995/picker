package com.grasswort.picker.user.dao.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "pk_user_oss_ref")
public class UserOssRef {
    @Id
    private Long id;

    @Column(name = "oss_bucket")
    private String ossBucket;

    @Column(name = "oss_key")
    private String ossKey;

    @Column(name = "oss_ref_id")
    private Long ossRefId;

    @Column(name = "oss_ref_del_key")
    private String ossRefDelKey;

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
     * @return oss_bucket
     */
    public String getOssBucket() {
        return ossBucket;
    }

    /**
     * @param ossBucket
     */
    public void setOssBucket(String ossBucket) {
        this.ossBucket = ossBucket;
    }

    /**
     * @return oss_key
     */
    public String getOssKey() {
        return ossKey;
    }

    /**
     * @param ossKey
     */
    public void setOssKey(String ossKey) {
        this.ossKey = ossKey;
    }

    /**
     * @return oss_ref_id
     */
    public Long getOssRefId() {
        return ossRefId;
    }

    /**
     * @param ossRefId
     */
    public void setOssRefId(Long ossRefId) {
        this.ossRefId = ossRefId;
    }

    /**
     * @return oss_ref_del_key
     */
    public String getOssRefDelKey() {
        return ossRefDelKey;
    }

    /**
     * @param ossRefDelKey
     */
    public void setOssRefDelKey(String ossRefDelKey) {
        this.ossRefDelKey = ossRefDelKey;
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