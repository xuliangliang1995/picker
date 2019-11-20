package com.grasswort.picker.blog.dto;

import com.grasswort.picker.commons.result.AbstractRequest;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author xuliangliang
 * @Classname BlogPoolQueryRequest
 * @Description
 * @Date 2019/11/20 15:49
 * @blame Java Team
 */
@Data
public class BlogPoolQueryRequest extends AbstractRequest {

    private String keyword;

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
        private String keyword;
        private Integer pageNo;
        private Integer pageSize;

        private Builder() {
        }

        public static Builder aBlogPoolQueryRequest() {
            return new Builder();
        }

        public Builder withKeyword(String keyword) {
            this.keyword = keyword;
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

        public BlogPoolQueryRequest build() {
            BlogPoolQueryRequest blogPoolQueryRequest = new BlogPoolQueryRequest();
            blogPoolQueryRequest.setKeyword(keyword);
            blogPoolQueryRequest.setPageNo(pageNo);
            blogPoolQueryRequest.setPageSize(pageSize);
            return blogPoolQueryRequest;
        }
    }
}
