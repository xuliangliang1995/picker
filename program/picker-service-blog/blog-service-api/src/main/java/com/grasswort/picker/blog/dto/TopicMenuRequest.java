package com.grasswort.picker.blog.dto;

import com.grasswort.picker.commons.result.AbstractRequest;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author xuliangliang
 * @Classname TopicMenuRequest
 * @Description
 * @Date 2019/12/11 15:20
 * @blame Java Team
 */
@Data
public class TopicMenuRequest extends AbstractRequest {

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

        public static Builder aTopicMenuRequest() {
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

        public TopicMenuRequest build() {
            TopicMenuRequest topicMenuRequest = new TopicMenuRequest();
            topicMenuRequest.setPkUserId(pkUserId);
            topicMenuRequest.setTopicId(topicId);
            return topicMenuRequest;
        }
    }
}
