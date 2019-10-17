package com.grasswort.picker.oss.dao.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "pk_oss_key")
public class OssKey {
    @Id
    private Long id;

    /**
     * OSS 仓库名
     */
    @Column(name = "oss_bucket")
    private String ossBucket;

    /**
     * OSS 存储 key
     */
    @Column(name = "oss_key")
    private String ossKey;

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
     * 获取OSS 仓库名
     *
     * @return oss_bucket - OSS 仓库名
     */
    public String getOssBucket() {
        return ossBucket;
    }

    /**
     * 设置OSS 仓库名
     *
     * @param ossBucket OSS 仓库名
     */
    public void setOssBucket(String ossBucket) {
        this.ossBucket = ossBucket;
    }

    /**
     * 获取OSS 存储 key
     *
     * @return oss_key - OSS 存储 key
     */
    public String getOssKey() {
        return ossKey;
    }

    /**
     * 设置OSS 存储 key
     *
     * @param ossKey OSS 存储 key
     */
    public void setOssKey(String ossKey) {
        this.ossKey = ossKey;
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