package com.grasswort.picker.blog.dao.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "pk_blog_label")
public class BlogLabel {
    @Id
    private Long id;

    @Column(name = "blog_id")
    private Long blogId;

    private String label;

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
     * @return label
     */
    public String getLabel() {
        return label;
    }

    /**
     * @param label
     */
    public void setLabel(String label) {
        this.label = label;
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