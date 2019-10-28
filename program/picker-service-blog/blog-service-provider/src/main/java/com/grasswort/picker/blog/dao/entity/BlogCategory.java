package com.grasswort.picker.blog.dao.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "pk_blog_category")
public class BlogCategory {
    @Id
    private Long id;

    @Column(name = "pk_user_id")
    private Long pkUserId;

    private String category;

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
     * @return category
     */
    public String getCategory() {
        return category;
    }

    /**
     * @return pkUserId
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
     * @param category
     */
    public void setCategory(String category) {
        this.category = category;
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