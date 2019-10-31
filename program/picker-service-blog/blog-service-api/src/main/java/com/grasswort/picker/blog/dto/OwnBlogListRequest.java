package com.grasswort.picker.blog.dto;

import com.grasswort.picker.commons.result.AbstractRequest;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author xuliangliang
 * @Classname BlogListRequest
 * @Description 博客列表
 * @Date 2019/10/30 20:18
 * @blame Java Team
 */
@Data
public class OwnBlogListRequest extends AbstractRequest {
    @NotNull
    @Min(1)
    private Long userId;

    private Long categoryId;

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
        private Long categoryId;
        private Integer pageNo;
        private Integer pageSize;

        private Builder() {
        }

        public static Builder anOwnBlogListRequest() {
            return new Builder();
        }

        public Builder withUserId(Long userId) {
            this.userId = userId;
            return this;
        }

        public Builder withCategoryId(Long categoryId) {
            this.categoryId = categoryId;
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

        public OwnBlogListRequest build() {
            OwnBlogListRequest ownBlogListRequest = new OwnBlogListRequest();
            ownBlogListRequest.setUserId(userId);
            ownBlogListRequest.setCategoryId(categoryId);
            ownBlogListRequest.setPageNo(pageNo);
            ownBlogListRequest.setPageSize(pageSize);
            return ownBlogListRequest;
        }
    }
}
