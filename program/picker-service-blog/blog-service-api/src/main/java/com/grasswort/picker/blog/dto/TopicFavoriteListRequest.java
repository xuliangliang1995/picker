package com.grasswort.picker.blog.dto;

import com.grasswort.picker.commons.result.AbstractRequest;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author xuliangliang
 * @Classname TopicFavoriteListRequest
 * @Description
 * @Date 2019/12/30
 * @blame Java Team
 */
@Data
public class TopicFavoriteListRequest extends AbstractRequest {
    @NotEmpty
    private Long authorId;
    @Min(1)
    private Long pickerId;
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
        private Long authorId;
        private Long pickerId;
        private Integer pageNo;
        private Integer pageSize;

        private Builder() {
        }

        public static Builder aTopicFavoriteListRequest() {
            return new Builder();
        }

        public Builder withAuthorId(Long authorId) {
            this.authorId = authorId;
            return this;
        }

        public Builder withPickerId(Long pickerId) {
            this.pickerId = pickerId;
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

        public TopicFavoriteListRequest build() {
            TopicFavoriteListRequest topicFavoriteListRequest = new TopicFavoriteListRequest();
            topicFavoriteListRequest.setAuthorId(authorId);
            topicFavoriteListRequest.setPickerId(pickerId);
            topicFavoriteListRequest.setPageNo(pageNo);
            topicFavoriteListRequest.setPageSize(pageSize);
            return topicFavoriteListRequest;
        }
    }
}
