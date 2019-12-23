package com.grasswort.picker.blog.dto;

import com.grasswort.picker.commons.result.AbstractRequest;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author xuliangliang
 * @Classname TopicPoolRequest
 * @Description
 * @Date 2019/12/19 13:51
 * @blame Java Team
 */
@Data
public class TopicPoolRequest extends AbstractRequest {

    private String keyword;

    @Min(1)
    private Long authorId;

    @NotNull
    @Min(1)
    private Integer pageNo;

    @NotNull
    @Min(1)
    @Max(100)
    private Integer pageSize;

    @Min(1)
    private Long browseUserId;

    @Override
    public void requestCheck() {

    }

    public static final class Builder {
        private String keyword;
        private Long authorId;
        private Integer pageNo;
        private Integer pageSize;
        private Long browseUserId;

        private Builder() {
        }

        public static Builder aTopicPoolRequest() {
            return new Builder();
        }

        public Builder withKeyword(String keyword) {
            this.keyword = keyword;
            return this;
        }

        public Builder withAuthorId(Long authorId) {
            this.authorId = authorId;
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

        public Builder withBrowseUserId(Long browseUserId) {
            this.browseUserId = browseUserId;
            return this;
        }

        public TopicPoolRequest build() {
            TopicPoolRequest topicPoolRequest = new TopicPoolRequest();
            topicPoolRequest.setKeyword(keyword);
            topicPoolRequest.setAuthorId(authorId);
            topicPoolRequest.setPageNo(pageNo);
            topicPoolRequest.setPageSize(pageSize);
            topicPoolRequest.setBrowseUserId(browseUserId);
            return topicPoolRequest;
        }
    }
}
