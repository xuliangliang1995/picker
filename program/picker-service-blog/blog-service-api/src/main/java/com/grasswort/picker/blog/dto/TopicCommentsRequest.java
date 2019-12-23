package com.grasswort.picker.blog.dto;

import com.grasswort.picker.commons.result.AbstractRequest;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author xuliangliang
 * @Classname TopicCommentsRequest
 * @Description
 * @Date 2019/12/23 16:01
 * @blame Java Team
 */
@Data
public class TopicCommentsRequest extends AbstractRequest {

    @Min(1)
    private Long userId;
    @NotEmpty
    private String topicId;
    @NotNull
    @Min(1)
    private Integer pageNo;
    @NotNull
    @Min(1)
    @Max(100)
    private Integer pageSize;

    @Override
    public void requestCheck() {

    }

    public static final class Builder {
        private Long userId;
        private String topicId;
        private Integer pageNo;
        private Integer pageSize;

        private Builder() {
        }

        public static Builder aTopicCommentsRequest() {
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

        public Builder withPageNo(Integer pageNo) {
            this.pageNo = pageNo;
            return this;
        }

        public Builder withPageSize(Integer pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        public TopicCommentsRequest build() {
            TopicCommentsRequest topicCommentsRequest = new TopicCommentsRequest();
            topicCommentsRequest.setUserId(userId);
            topicCommentsRequest.setTopicId(topicId);
            topicCommentsRequest.setPageNo(pageNo);
            topicCommentsRequest.setPageSize(pageSize);
            return topicCommentsRequest;
        }
    }
}
