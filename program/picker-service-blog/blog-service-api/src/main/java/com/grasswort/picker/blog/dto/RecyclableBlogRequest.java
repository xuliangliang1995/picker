package com.grasswort.picker.blog.dto;

import com.grasswort.picker.commons.result.AbstractRequest;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author xuliangliang
 * @Classname RecyclableBlogRequest
 * @Description 可回收博客请求
 * @Date 2019/12/5 13:49
 * @blame Java Team
 */
@Data
public class RecyclableBlogRequest extends AbstractRequest {
    @NotNull
    @Min(1)
    private Long userId;
    @NotNull
    @Min(1)
    private Integer pageNo;
    @NotNull
    @Max(100)
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

        public static Builder aRecyclableBlogRequest() {
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

        public RecyclableBlogRequest build() {
            RecyclableBlogRequest recyclableBlogRequest = new RecyclableBlogRequest();
            recyclableBlogRequest.setUserId(userId);
            recyclableBlogRequest.setPageNo(pageNo);
            recyclableBlogRequest.setPageSize(pageSize);
            return recyclableBlogRequest;
        }
    }
}
