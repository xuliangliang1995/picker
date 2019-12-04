package com.grasswort.picker.user.dto;

import com.grasswort.picker.commons.result.AbstractRequest;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author xuliangliang
 * @Classname SearchRequest
 * @Description 查询用户数量
 * @Date 2019/12/3 23:01
 * @blame Java Team
 */
@Data
public class UserSearchRequest extends AbstractRequest {
    @NotNull
    @Min(1)
    private Integer pageNo;
    @NotNull
    @Max(100)
    private Integer pageSize;

    private String keyword;

    private Long pkUserId;
    @Override
    public void requestCheck() {

    }

    public static final class Builder {
        private Integer pageNo;
        private Integer pageSize;
        private String keyword;
        private Long pkUserId;

        private Builder() {
        }

        public static Builder anUserSearchRequest() {
            return new Builder();
        }

        public Builder withPageNo(Integer pageNo) {
            this.pageNo = pageNo;
            return this;
        }

        public Builder withPageSize(Integer pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        public Builder withKeyword(String keyword) {
            this.keyword = keyword;
            return this;
        }

        public Builder withPkUserId(Long pkUserId) {
            this.pkUserId = pkUserId;
            return this;
        }

        public UserSearchRequest build() {
            UserSearchRequest userSearchRequest = new UserSearchRequest();
            userSearchRequest.setPageNo(pageNo);
            userSearchRequest.setPageSize(pageSize);
            userSearchRequest.setKeyword(keyword);
            userSearchRequest.setPkUserId(pkUserId);
            return userSearchRequest;
        }
    }
}
