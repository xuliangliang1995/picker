package com.grasswort.picker.blog.dto.blog;

import lombok.Data;

import java.util.Date;

/**
 * @author xuliangliang
 * @Classname BlogItem
 * @Description 博客
 * @Date 2019/10/30 22:18
 * @blame Java Team
 */
@Data
public class BlogItem {
    /**
     * 博客 id
     */
    private Long blogId;
    /**
     * 标题
     */
    private String title;
    /**
     * 版本
     */
    private Integer version;
    /**
     * 创建时间
     */
    private Date gmtCreate;
    /**
     * 最后一次更新时间
     */
    private Date gmtModified;


    public static final class Builder {
        private Long blogId;
        private String title;
        private Integer version;
        private Date gmtCreate;
        private Date gmtModified;

        private Builder() {
        }

        public static Builder aBlogItem() {
            return new Builder();
        }

        public Builder withBlogId(Long blogId) {
            this.blogId = blogId;
            return this;
        }

        public Builder withTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder withVersion(Integer version) {
            this.version = version;
            return this;
        }

        public Builder withGmtCreate(Date gmtCreate) {
            this.gmtCreate = gmtCreate;
            return this;
        }

        public Builder withGmtModified(Date gmtModified) {
            this.gmtModified = gmtModified;
            return this;
        }

        public BlogItem build() {
            BlogItem blogItem = new BlogItem();
            blogItem.setBlogId(blogId);
            blogItem.setTitle(title);
            blogItem.setVersion(version);
            blogItem.setGmtCreate(gmtCreate);
            blogItem.setGmtModified(gmtModified);
            return blogItem;
        }
    }
}
