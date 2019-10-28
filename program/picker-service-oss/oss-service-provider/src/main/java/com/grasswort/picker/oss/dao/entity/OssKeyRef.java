package com.grasswort.picker.oss.dao.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "pk_oss_key_ref")
public class OssKeyRef {
    @Id
    private Long id;

    @Column(name = "oss_key_id")
    private Long ossKeyId;

    /**
     * 删除引用的钥匙，提交引用的一方需要自己记录一份
     */
    @Column(name = "del_key")
    private String delKey;

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
     * @return oss_key_id
     */
    public Long getOssKeyId() {
        return ossKeyId;
    }

    /**
     * @param ossKeyId
     */
    public void setOssKeyId(Long ossKeyId) {
        this.ossKeyId = ossKeyId;
    }

    /**
     * 获取删除引用的钥匙，提交引用的一方需要自己记录一份
     *
     * @return del_key - 删除引用的钥匙，提交引用的一方需要自己记录一份
     */
    public String getDelKey() {
        return delKey;
    }

    /**
     * 设置删除引用的钥匙，提交引用的一方需要自己记录一份
     *
     * @param delKey 删除引用的钥匙，提交引用的一方需要自己记录一份
     */
    public void setDelKey(String delKey) {
        this.delKey = delKey;
    }

    /**
     * @return gmt_created
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