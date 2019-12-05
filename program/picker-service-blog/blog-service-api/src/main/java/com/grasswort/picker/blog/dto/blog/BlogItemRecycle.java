package com.grasswort.picker.blog.dto.blog;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author xuliangliang
 * @Classname BlogItemRecycle
 * @Description 可回收博客（带回收截止时间）
 * @Date 2019/12/5 14:11
 * @blame Java Team
 */
@Data
public class BlogItemRecycle extends BlogItem {
    /**
     * 回收 deadline
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date recycleDeadline;


    public static final class Builder {
        private InteractionData interaction;
        private Date recycleDeadline;
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

        public static Builder aBlogItemRecycle() {
            return new Builder();
        }

        public Builder withInteraction(InteractionData interaction) {
            this.interaction = interaction;
            return this;
        }

        public Builder withRecycleDeadline(Date recycleDeadline) {
            this.recycleDeadline = recycleDeadline;
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

        public BlogItemRecycle build() {
            BlogItemRecycle blogItemRecycle = new BlogItemRecycle();
            blogItemRecycle.setInteraction(interaction);
            blogItemRecycle.setRecycleDeadline(recycleDeadline);
            blogItemRecycle.setPickerId(pickerId);
            blogItemRecycle.setBlogId(blogId);
            blogItemRecycle.setTitle(title);
            blogItemRecycle.setSummary(summary);
            blogItemRecycle.setCoverImg(coverImg);
            blogItemRecycle.setCategoryId(categoryId);
            blogItemRecycle.setCategory(category);
            blogItemRecycle.setLabels(labels);
            blogItemRecycle.setVersion(version);
            blogItemRecycle.setTriggerStatus(triggerStatus);
            blogItemRecycle.setGmtCreate(gmtCreate);
            blogItemRecycle.setGmtModified(gmtModified);
            return blogItemRecycle;
        }
    }
}
