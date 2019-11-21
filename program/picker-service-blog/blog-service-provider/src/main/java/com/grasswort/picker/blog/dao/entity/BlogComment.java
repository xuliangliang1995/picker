package com.grasswort.picker.blog.dao.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "pk_blog_comment")
public class BlogComment {
    @Id
    private Long id;

    @Column(name = "pk_blog_id")
    private Long pkBlogId;

    @Column(name = "reply_comment_id")
    private Long replyCommentId;

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
     * @return pk_blog_id
     */
    public Long getPkBlogId() {
        return pkBlogId;
    }

    /**
     * @param pkBlogId
     */
    public void setPkBlogId(Long pkBlogId) {
        this.pkBlogId = pkBlogId;
    }

    /**
     * @return reply_comment_id
     */
    public Long getReplyCommentId() {
        return replyCommentId;
    }

    /**
     * @param replyCommentId
     */
    public void setReplyCommentId(Long replyCommentId) {
        this.replyCommentId = replyCommentId;
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