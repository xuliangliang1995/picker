package com.grasswort.picker.blog.dto;

import com.grasswort.picker.commons.result.AbstractRequest;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author xuliangliang
 * @Classname TopicFavoriteCancelRequest
 * @Description
 * @Date 2019/12/20 14:55
 * @blame Java Team
 */
@Data
public class TopicFavoriteCancelRequest extends AbstractRequest {
    @NotNull
    @Min(1)
    private Long userId;
    @NotEmpty
    private String topicId;

    @Override
    public void requestCheck() {

    }

    public static final class Builder {
        private Long userId;
        private String topicId;

        private Builder() {
        }

        public static Builder aTopicFavoriteCancelRequest() {
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

        public TopicFavoriteCancelRequest build() {
            TopicFavoriteCancelRequest topicFavoriteCancelRequest = new TopicFavoriteCancelRequest();
            topicFavoriteCancelRequest.setUserId(userId);
            topicFavoriteCancelRequest.setTopicId(topicId);
            return topicFavoriteCancelRequest;
        }
    }
}
