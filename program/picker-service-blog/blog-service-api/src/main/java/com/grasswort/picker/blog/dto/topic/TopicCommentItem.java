package com.grasswort.picker.blog.dto.topic;

import lombok.Data;

import java.util.Date;

/**
 * @author xuliangliang
 * @Classname TopicComment
 * @Description
 * @Date 2019/12/23 16:54
 * @blame Java Team
 */
@Data
public class TopicCommentItem {

    private String pickerId;

    private String pickerAvatar;

    private String pickerName;

    private Integer pickerSex;

    private String content;

    private Integer rate;

    private Date gmtCreate;

    public static final class Builder {
        private String pickerId;
        private String pickerAvatar;
        private String pickerName;
        private Integer pickerSex;
        private String content;
        private Integer rate;
        private Date gmtCreate;

        private Builder() {
        }

        public static Builder aTopicCommentItem() {
            return new Builder();
        }

        public Builder withPickerId(String pickerId) {
            this.pickerId = pickerId;
            return this;
        }

        public Builder withPickerAvatar(String pickerAvatar) {
            this.pickerAvatar = pickerAvatar;
            return this;
        }

        public Builder withPickerName(String pickerName) {
            this.pickerName = pickerName;
            return this;
        }

        public Builder withPickerSex(Integer pickerSex) {
            this.pickerSex = pickerSex;
            return this;
        }

        public Builder withContent(String content) {
            this.content = content;
            return this;
        }

        public Builder withRate(Integer rate) {
            this.rate = rate;
            return this;
        }

        public Builder withGmtCreate(Date gmtCreate) {
            this.gmtCreate = gmtCreate;
            return this;
        }

        public TopicCommentItem build() {
            TopicCommentItem topicCommentItem = new TopicCommentItem();
            topicCommentItem.setPickerId(pickerId);
            topicCommentItem.setPickerAvatar(pickerAvatar);
            topicCommentItem.setPickerName(pickerName);
            topicCommentItem.setPickerSex(pickerSex);
            topicCommentItem.setContent(content);
            topicCommentItem.setRate(rate);
            topicCommentItem.setGmtCreate(gmtCreate);
            return topicCommentItem;
        }
    }
}
