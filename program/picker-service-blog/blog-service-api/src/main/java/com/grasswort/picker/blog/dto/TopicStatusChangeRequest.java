package com.grasswort.picker.blog.dto;

import com.grasswort.picker.commons.result.AbstractRequest;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author xuliangliang
 * @Classname TopicStatusChangeRequest
 * @Description 专题状态切换（私密/公开）
 * @Date 2019/12/13 9:33
 * @blame Java Team
 */
@Data
public class TopicStatusChangeRequest extends AbstractRequest {
    @NotNull
    @Min(1)
    private Long pkUserId;

    @NotEmpty
    private String topicId;

    @NotNull
    @Min(0)
    @Max(1)
    private Integer status;

    @Override
    public void requestCheck() {

    }

    public static final class Builder {
        private Long pkUserId;
        private String topicId;
        private Integer status;

        private Builder() {
        }

        public static Builder aTopicStatusChangeRequest() {
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

        public Builder withStatus(Integer status) {
            this.status = status;
            return this;
        }

        public TopicStatusChangeRequest build() {
            TopicStatusChangeRequest topicStatusChangeRequest = new TopicStatusChangeRequest();
            topicStatusChangeRequest.setPkUserId(pkUserId);
            topicStatusChangeRequest.setTopicId(topicId);
            topicStatusChangeRequest.setStatus(status);
            return topicStatusChangeRequest;
        }
    }
}
