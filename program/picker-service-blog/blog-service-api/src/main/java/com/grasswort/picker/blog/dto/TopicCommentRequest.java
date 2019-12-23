package com.grasswort.picker.blog.dto;

import com.grasswort.picker.commons.result.AbstractRequest;
import lombok.Data;

import javax.validation.constraints.*;

/**
 * @author xuliangliang
 * @Classname TopicCommentRequest
 * @Description
 * @Date 2019/12/23 15:30
 * @blame Java Team
 */
@Data
public class TopicCommentRequest extends AbstractRequest {

    @NotNull
    @Min(1)
    private Long userId;
    @NotEmpty
    private String topicId;
    @Size(max = 500)
    private String content;
    @NotNull
    @Min(1)
    @Max(5)
    private Integer rate;

    @Override
    public void requestCheck() {

    }

    public static final class Builder {
        private Long userId;
        private String topicId;
        private String content;
        private Integer rate;

        private Builder() {
        }

        public static Builder aTopicCommentRequest() {
            return new Builder();
        }

        public Builder withUserId(Long userId) {
            this.userId = userId;
            return this;
        }

        public Builder withTopicId(String topicId) {
            this.topicId = topicId;
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

        public TopicCommentRequest build() {
            TopicCommentRequest topicCommentRequest = new TopicCommentRequest();
            topicCommentRequest.setUserId(userId);
            topicCommentRequest.setTopicId(topicId);
            topicCommentRequest.setContent(content);
            topicCommentRequest.setRate(rate);
            return topicCommentRequest;
        }
    }
}
