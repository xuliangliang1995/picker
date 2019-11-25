package com.grasswort.picker.blog.dto.blog;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.grasswort.picker.blog.constant.BlogCurveStatusEnum;
import lombok.Data;

import java.util.Date;
import java.util.List;

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
     * 交互数据
     */
    private InteractionData interaction;
    /**
     * 作者ID
     */
    private String pickerId;
    /**
     * 博客 ID
     */
    private String blogId;
    /**
     * 标题
     */
    private String title;
    /**
     * 摘要
     */
    private String summary;
    /**
     * 封面配图
     */
    private String coverImg;
    /**
     * 分类 id
     */
    private Long categoryId;
    /**
     * 分类
     */
    private String category;
    /**
     * 标签
     */
    private List<String> labels;
    /**
     * 版本
     */
    private Integer version;
    /**
     * 推送触发状态（
     * @see BlogCurveStatusEnum
     */
    private Integer triggerStatus;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date gmtCreate;
    /**
     * 最后一次更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date gmtModified;


    public static final class Builder {
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

        public static Builder aBlogItem() {
            return new Builder();
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

        public BlogItem build() {
            BlogItem blogItem = new BlogItem();
            blogItem.setPickerId(pickerId);
            blogItem.setBlogId(blogId);
            blogItem.setTitle(title);
            blogItem.setSummary(summary);
            blogItem.setCoverImg(coverImg);
            blogItem.setCategoryId(categoryId);
            blogItem.setCategory(category);
            blogItem.setLabels(labels);
            blogItem.setVersion(version);
            blogItem.setTriggerStatus(triggerStatus);
            blogItem.setGmtCreate(gmtCreate);
            blogItem.setGmtModified(gmtModified);
            return blogItem;
        }
    }
}
