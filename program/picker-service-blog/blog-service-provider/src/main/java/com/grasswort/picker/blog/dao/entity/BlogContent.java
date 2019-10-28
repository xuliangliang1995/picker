package com.grasswort.picker.blog.dao.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "pk_blog_content")
public class BlogContent {
    @Id
    private Long id;

    @Column(name = "blog_id")
    private Long blogId;

    @Column(name = "blog_version")
    private Integer blogVersion;

    @Column(name = "gmt_create")
    private Date gmtCreate;

    @Column(name = "gmt_modified")
    private Date gmtModified;

    private String html;

    private String markdown;

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
     * @return blog_version
     */
    public Integer getBlogVersion() {
        return blogVersion;
    }

    /**
     * @param blogVersion
     */
    public void setBlogVersion(Integer blogVersion) {
        this.blogVersion = blogVersion;
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

    /**
     * @return html
     */
    public String getHtml() {
        return html;
    }

    /**
     * @param html
     */
    public void setHtml(String html) {
        this.html = html;
    }

    /**
     * @return markdown
     */
    public String getMarkdown() {
        return markdown;
    }

    /**
     * @param markdown
     */
    public void setMarkdown(String markdown) {
        this.markdown = markdown;
    }
}