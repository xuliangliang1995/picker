package com.grasswort.picker.blog.dao.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "pk_blog_oss_ref")
public class BlogOssRef {
    @Id
    private Long id;

    @Column(name = "blog_id")
    private Long blogId;

    @Column(name = "oss_bucket")
    private String ossBucket;

    @Column(name = "oss_key")
    private String ossKey;

    /**
     * 在 oss 服务器创建引用后，服务端返回的引用id，引用无效后根据此id删除 oss 服务器引用
     */
    @Column(name = "oss_ref_id")
    private Long ossRefId;

    /**
     * 删除引用需要的 key ,防止他人删除
     */
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
     * @return blog_id
     */
    public Long getBlogId() {
        return blogId;
    }

    /**
     * @param blogId
     */
    public void setBlogId(Long blogId) {
        this.blogId = blogId;
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
     * 获取在 oss 服务器创建引用后，服务端返回的引用id，引用无效后根据此id删除 oss 服务器引用
     *
     * @return oss_ref_id - 在 oss 服务器创建引用后，服务端返回的引用id，引用无效后根据此id删除 oss 服务器引用
     */
    public Long getOssRefId() {
        return ossRefId;
    }

    /**
     * 设置在 oss 服务器创建引用后，服务端返回的引用id，引用无效后根据此id删除 oss 服务器引用
     *
     * @param ossRefId 在 oss 服务器创建引用后，服务端返回的引用id，引用无效后根据此id删除 oss 服务器引用
     */
    public void setOssRefId(Long ossRefId) {
        this.ossRefId = ossRefId;
    }

    /**
     * 获取删除引用需要的 key ,防止他人删除
     *
     * @return oss_ref_del_key - 删除引用需要的 key ,防止他人删除
     */
    public String getOssRefDelKey() {
        return ossRefDelKey;
    }

    /**
     * 设置删除引用需要的 key ,防止他人删除
     *
     * @param ossRefDelKey 删除引用需要的 key ,防止他人删除
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