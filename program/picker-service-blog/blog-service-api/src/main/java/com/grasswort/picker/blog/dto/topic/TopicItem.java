package com.grasswort.picker.blog.dto.topic;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author xuliangliang
 * @Classname TopicItem
 * @Description 专题
 * @Date 2019/12/10 15:41
 * @blame Java Team
 */
@Data
public class TopicItem {

    private String topicId;

    private String pkUserId;

    private String ownerName;

    private String ownerAvatar;

    private String title;

    private String coverImg;

    private String summary;

    private Integer status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date gmtCreate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date gmtModified;

    public static final class Builder {
        private String topicId;
        private String pkUserId;
        private String ownerName;
        private String ownerAvatar;
        private String title;
        private String coverImg;
        private String summary;
        private Integer status;
        private Date gmtCreate;
        private Date gmtModified;

        private Builder() {
        }

        public static Builder aTopicItem() {
            return new Builder();
        }

        public Builder withTopicId(String topicId) {
            this.topicId = topicId;
            return this;
        }

        public Builder withPkUserId(String pkUserId) {
            this.pkUserId = pkUserId;
            return this;
        }

        public Builder withOwnerName(String ownerName) {
            this.ownerName = ownerName;
            return this;
        }

        public Builder withOwnerAvatar(String ownerAvatar) {
            this.ownerAvatar = ownerAvatar;
            return this;
        }

        public Builder withTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder withCoverImg(String coverImg) {
            this.coverImg = coverImg;
            return this;
        }

        public Builder withSummary(String summary) {
            this.summary = summary;
            return this;
        }

        public Builder withStatus(Integer status) {
            this.status = status;
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

        public TopicItem build() {
            TopicItem topicItem = new TopicItem();
            topicItem.setTopicId(topicId);
            topicItem.setPkUserId(pkUserId);
            topicItem.setOwnerName(ownerName);
            topicItem.setOwnerAvatar(ownerAvatar);
            topicItem.setTitle(title);
            topicItem.setCoverImg(coverImg);
            topicItem.setSummary(summary);
            topicItem.setStatus(status);
            topicItem.setGmtCreate(gmtCreate);
            topicItem.setGmtModified(gmtModified);
            return topicItem;
        }
    }
}
