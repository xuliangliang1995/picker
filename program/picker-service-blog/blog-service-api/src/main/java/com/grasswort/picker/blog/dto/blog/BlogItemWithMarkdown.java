package com.grasswort.picker.blog.dto.blog;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author xuliangliang
 * @Classname BlogItemWithMarkdown
 * @Description 获取 Blog 内容
 * @Date 2019/10/31 18:28
 * @blame Java Team
 */
@Data
public class BlogItemWithMarkdown extends BlogItem {

    private String markdown;

    public static final class Builder {
        private String markdown;
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

        public static Builder aBlogItemWithMarkdown() {
            return new Builder();
        }

        public Builder withMarkdown(String markdown) {
            this.markdown = markdown;
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

        public BlogItemWithMarkdown build() {
            BlogItemWithMarkdown blogItemWithMarkdown = new BlogItemWithMarkdown();
            blogItemWithMarkdown.setMarkdown(markdown);
            blogItemWithMarkdown.setPickerId(pickerId);
            blogItemWithMarkdown.setBlogId(blogId);
            blogItemWithMarkdown.setTitle(title);
            blogItemWithMarkdown.setSummary(summary);
            blogItemWithMarkdown.setCoverImg(coverImg);
            blogItemWithMarkdown.setCategoryId(categoryId);
            blogItemWithMarkdown.setCategory(category);
            blogItemWithMarkdown.setLabels(labels);
            blogItemWithMarkdown.setVersion(version);
            blogItemWithMarkdown.setTriggerStatus(triggerStatus);
            blogItemWithMarkdown.setGmtCreate(gmtCreate);
            blogItemWithMarkdown.setGmtModified(gmtModified);
            return blogItemWithMarkdown;
        }
    }
}
