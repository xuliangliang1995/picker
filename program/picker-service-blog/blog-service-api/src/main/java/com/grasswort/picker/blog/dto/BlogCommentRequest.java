package com.grasswort.picker.blog.dto;

import com.grasswort.picker.commons.result.AbstractRequest;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author xuliangliang
 * @Classname BlogCommentRequest
 * @Description 博客评论
 * @Date 2019/11/21 22:07
 * @blame Java Team
 */
@Data
public class BlogCommentRequest extends AbstractRequest {
    @NotEmpty
    private String blogId;
    @NotNull
    @Min(1)
    private Integer pageNo;
    @NotNull
    @Min(1)
    @Max(10)
    private Integer pageSize;
    @Override
    public void requestCheck() {

    }

    public static final class Builder {
        private String blogId;
        private Integer pageNo;
        private Integer pageSize;

        private Builder() {
        }

        public static Builder aBlogCommentRequest() {
            return new Builder();
        }

        public Builder withBlogId(String blogId) {
            this.blogId = blogId;
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

        public BlogCommentRequest build() {
            BlogCommentRequest blogCommentRequest = new BlogCommentRequest();
            blogCommentRequest.setBlogId(blogId);
            blogCommentRequest.setPageNo(pageNo);
            blogCommentRequest.setPageSize(pageSize);
            return blogCommentRequest;
        }
    }
}
