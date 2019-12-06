package com.grasswort.picker.user.dto;

import com.grasswort.picker.commons.result.AbstractRequest;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author xuliangliang
 * @Classname FollowerRequest
 * @Description 粉丝列表
 * @Date 2019/12/6 14:34
 * @blame Java Team
 */
@Data
public class FollowerRequest extends AbstractRequest {
    @Min(1)
    @NotNull
    private Long userId;
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
        private Integer pageNo;
        private Integer pageSize;

        private Builder() {
        }

        public static Builder aFollowerRequest() {
            return new Builder();
        }

        public Builder withUserId(Long userId) {
            this.userId = userId;
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

        public FollowerRequest build() {
            FollowerRequest followerRequest = new FollowerRequest();
            followerRequest.setUserId(userId);
            followerRequest.setPageNo(pageNo);
            followerRequest.setPageSize(pageSize);
            return followerRequest;
        }
    }
}
