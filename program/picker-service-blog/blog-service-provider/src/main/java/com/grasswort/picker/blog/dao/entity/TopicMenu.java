package com.grasswort.picker.blog.dao.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "pk_topic_menu")
public class TopicMenu {
    @Id
    private Long id;

    @Column(name = "topic_id")
    private Long topicId;

    @Column(name = "parent_menu_id")
    private Long parentMenuId;

    @Column(name = "menu_name")
    private String menuName;

    @Column(name = "menu_type")
    private Integer menuType;

    @Column(name = "blog_id")
    private Long blogId;

    private Integer weight;

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
     * @return topic_id
     */
    public Long getTopicId() {
        return topicId;
    }

    /**
     * @param topicId
     */
    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }

    /**
     * @return parent_menu_id
     */
    public Long getParentMenuId() {
        return parentMenuId;
    }

    /**
     * @param parentMenuId
     */
    public void setParentMenuId(Long parentMenuId) {
        this.parentMenuId = parentMenuId;
    }

    /**
     * @return menu_name
     */
    public String getMenuName() {
        return menuName;
    }

    /**
     * @param menuName
     */
    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    /**
     * @return menu_type
     */
    public Integer getMenuType() {
        return menuType;
    }

    /**
     * @param menuType
     */
    public void setMenuType(Integer menuType) {
        this.menuType = menuType;
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
     * @return weight
     */
    public Integer getWeight() {
        return weight;
    }

    /**
     * @param weight
     */
    public void setWeight(Integer weight) {
        this.weight = weight;
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