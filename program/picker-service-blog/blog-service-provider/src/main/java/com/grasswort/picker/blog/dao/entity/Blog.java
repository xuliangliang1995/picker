package com.grasswort.picker.blog.dao.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "pk_blog")
public class Blog {
    @Id
    private Long id;

    @Column(name = "pk_user_id")
    private Long pkUserId;

    @Column(name = "category_id")
    private Long categoryId;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 封面配图
     */
    @Column(name = "cover_img")
    private String coverImg;

    /**
     * 文章摘要
     */
    private String summary;
    /**
     * 博客状态 0、正常 1、回收中 2、删除
     */
    private Integer status;
    /**
     * 博客的版本
     */
    private Integer version;

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
     * @return category_id
     */
    public Long getCategoryId() {
        return categoryId;
    }

    /**
     * @param categoryId
     */
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * 获取文章标题
     *
     * @return title - 文章标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置文章标题
     *
     * @param title 文章标题
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 获取封面配图
     *
     * @return cover_img - 封面配图
     */
    public String getCoverImg() {
        return coverImg;
    }

    /**
     * 设置封面配图
     *
     * @param coverImg 封面配图
     */
    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg;
    }

    /**
     * 获取文章摘要
     *
     * @return summary - 文章摘要
     */
    public String getSummary() {
        return summary;
    }

    /**
     * 设置文章摘要
     *
     * @param summary 文章摘要
     */
    public void setSummary(String summary) {
        this.summary = summary;
    }


    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取博客的版本
     *
     * @return version - 博客的版本
     */
    public Integer getVersion() {
        return version;
    }

    /**
     * 设置博客的版本
     *
     * @param version 博客的版本
     */
    public void setVersion(Integer version) {
        this.version = version;
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