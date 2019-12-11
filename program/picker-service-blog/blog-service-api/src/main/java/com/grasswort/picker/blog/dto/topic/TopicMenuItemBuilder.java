package com.grasswort.picker.blog.dto.topic;

import java.util.List;

/**
 * @author xuliangliang
 * @Classname TopicMenuItemBuilder
 * @Description TopicMenuItemBuilder
 * @Date 2019/12/11 15:48
 * @blame Java Team
 */
public final class TopicMenuItemBuilder {
    private Long menuId;
    private Long parentMenuId;
    private String menuName;
    private String menuType;
    private String blogId;
    private List<TopicMenuItem> children;

    private TopicMenuItemBuilder() {
    }

    public static TopicMenuItemBuilder aTopicMenuItem() {
        return new TopicMenuItemBuilder();
    }

    public TopicMenuItemBuilder withMenuId(Long menuId) {
        this.menuId = menuId;
        return this;
    }

    public TopicMenuItemBuilder withParentMenuId(Long parentMenuId) {
        this.parentMenuId = parentMenuId;
        return this;
    }

    public TopicMenuItemBuilder withMenuName(String menuName) {
        this.menuName = menuName;
        return this;
    }

    public TopicMenuItemBuilder withMenuType(String menuType) {
        this.menuType = menuType;
        return this;
    }

    public TopicMenuItemBuilder withBlogId(String blogId) {
        this.blogId = blogId;
        return this;
    }

    public TopicMenuItemBuilder withChildren(List<TopicMenuItem> children) {
        this.children = children;
        return this;
    }

    public TopicMenuItem build() {
        TopicMenuItem topicMenuItem = new TopicMenuItem();
        topicMenuItem.setMenuId(menuId);
        topicMenuItem.setParentMenuId(parentMenuId);
        topicMenuItem.setMenuName(menuName);
        topicMenuItem.setMenuType(menuType);
        topicMenuItem.setBlogId(blogId);
        topicMenuItem.setChildren(children);
        return topicMenuItem;
    }
}
