package com.grasswort.picker.blog.dto;

import com.grasswort.picker.commons.result.AbstractRequest;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author xuliangliang
 * @Classname TopicDeleteRequest
 * @Description
 * @Date 2019/12/13 10:28
 * @blame Java Team
 */
@Data
public class TopicDeleteRequest extends AbstractRequest {
    @NotEmpty
    private Long pkUserId;

    @NotEmpty
    private String topicId;

    @Override
    public void requestCheck() {

    }

    public static final class Builder {
        private Long pkUserId;
        private String topicId;

        private Builder() {
        }

        public static Builder aTopicDeleteRequest() {
            return new Builder();
        }

        public Builder withPkUserId(Long pkUserId) {
            this.pkUserId = pkUserId;
            return this;
        }

        public Builder withTopicId(String topicId) {
            this.topicId = topicId;
            return this;
        }

        public TopicDeleteRequest build() {
            TopicDeleteRequest topicDeleteRequest = new TopicDeleteRequest();
            topicDeleteRequest.setPkUserId(pkUserId);
            topicDeleteRequest.setTopicId(topicId);
            return topicDeleteRequest;
        }
    }
}
