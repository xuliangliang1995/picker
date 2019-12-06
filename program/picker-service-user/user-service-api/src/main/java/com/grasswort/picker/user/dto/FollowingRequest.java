package com.grasswort.picker.user.dto;

import com.grasswort.picker.commons.result.AbstractRequest;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author xuliangliang
 * @Classname FollowingRequest
 * @Description 关注列表
 * @Date 2019/12/6 14:37
 * @blame Java Team
 */
@Data
public class FollowingRequest extends AbstractRequest {
    @Min(1)
    private Long userId;
    @Min(1)
    @NotNull
    private Long authorId;
    @Min(1)
    @NotNull
    private Integer pageNo;
    @Max(100)
    @NotNull
    private Integer pageSize;

    @Override
    public void requestCheck() {

    }

    public static final class Builder {
        private Long userId;
        private Long authorId;
        private Integer pageNo;
        private Integer pageSize;

        private Builder() {
        }

        public static Builder aFollowingRequest() {
            return new Builder();
        }

        public Builder withUserId(Long userId) {
            this.userId = userId;
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

        public FollowingRequest build() {
            FollowingRequest followingRequest = new FollowingRequest();
            followingRequest.setUserId(userId);
            followingRequest.setAuthorId(authorId);
            followingRequest.setPageNo(pageNo);
            followingRequest.setPageSize(pageSize);
            return followingRequest;
        }
    }
}
