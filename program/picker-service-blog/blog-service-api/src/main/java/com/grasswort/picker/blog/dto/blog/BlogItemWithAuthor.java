package com.grasswort.picker.blog.dto.blog;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author xuliangliang
 * @Classname BlogItemWithAuthor
 * @Description 博客（包含作者信息）
 * @Date 2019/11/20 15:52
 * @blame Java Team
 */
@Data
public class BlogItemWithAuthor extends BlogItem {
    /**
     * 作者
     */
    private String author;
    /**
     * 作者头像
     */
    private String authorAvatar;


    public static final class Builder {
        private String author;
        private String authorAvatar;
        private String pickerId;
        private String blogId;
        private String title;
        private String summary;
        private String coverImg;
        private Long categoryId;
        private String category;
        private List<String> labels;
        private Integer version;
        private Integer triggerStatus;
        private Date gmtCreate;
        private Date gmtModified;

        private Builder() {
        }

        public static Builder aBlogItemWithAuthor() {
            return new Builder();
        }

        public Builder withAuthor(String author) {
            this.author = author;
            return this;
        }

        public Builder withAuthorAvatar(String authorAvatar) {
            this.authorAvatar = authorAvatar;
            return this;
        }

        public Builder withPickerId(String pickerId) {
            this.pickerId = pickerId;
            return this;
        }

        public Builder withBlogId(String blogId) {
            this.blogId = blogId;
            return this;
        }

        public Builder withTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder withSummary(String summary) {
            this.summary = summary;
            return this;
        }

        public Builder withCoverImg(String coverImg) {
            this.coverImg = coverImg;
            return this;
        }

        public Builder withCategoryId(Long categoryId) {
            this.categoryId = categoryId;
            return this;
        }

        public Builder withCategory(String category) {
            this.category = category;
            return this;
        }

        public Builder withLabels(List<String> labels) {
            this.labels = labels;
            return this;
        }

        public Builder withVersion(Integer version) {
            this.version = version;
            return this;
        }

        public Builder withTriggerStatus(Integer triggerStatus) {
            this.triggerStatus = triggerStatus;
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

        public BlogItemWithAuthor build() {
            BlogItemWithAuthor blogItemWithAuthor = new BlogItemWithAuthor();
            blogItemWithAuthor.setAuthor(author);
            blogItemWithAuthor.setAuthorAvatar(authorAvatar);
            blogItemWithAuthor.setPickerId(pickerId);
            blogItemWithAuthor.setBlogId(blogId);
            blogItemWithAuthor.setTitle(title);
            blogItemWithAuthor.setSummary(summary);
            blogItemWithAuthor.setCoverImg(coverImg);
            blogItemWithAuthor.setCategoryId(categoryId);
            blogItemWithAuthor.setCategory(category);
            blogItemWithAuthor.setLabels(labels);
            blogItemWithAuthor.setVersion(version);
            blogItemWithAuthor.setTriggerStatus(triggerStatus);
            blogItemWithAuthor.setGmtCreate(gmtCreate);
            blogItemWithAuthor.setGmtModified(gmtModified);
            return blogItemWithAuthor;
        }
    }
}
