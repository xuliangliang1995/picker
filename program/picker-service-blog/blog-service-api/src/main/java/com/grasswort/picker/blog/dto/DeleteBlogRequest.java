package com.grasswort.picker.blog.dto;

import com.grasswort.picker.commons.result.AbstractRequest;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author xuliangliang
 * @Classname DeleteBlogRequest
 * @Description 删除博客
 * @Date 2019/11/6 19:47
 * @blame Java Team
 */
@Data
public class DeleteBlogRequest extends AbstractRequest {
    @NotEmpty
    private String blogId;
    @NotNull
    @Min(1)
    private Long userId;

    @Override
    public void requestCheck() {

    }

    public static final class Builder {
        private String blogId;
        private Long userId;

        private Builder() {
        }

        public static Builder aDeleteBlogRequest() {
            return new Builder();
        }

        public Builder withBlogId(String blogId) {
            this.blogId = blogId;
            return this;
        }

        public Builder withUserId(Long userId) {
            this.userId = userId;
            return this;
        }

        public DeleteBlogRequest build() {
            DeleteBlogRequest deleteBlogRequest = new DeleteBlogRequest();
            deleteBlogRequest.setBlogId(blogId);
            deleteBlogRequest.setUserId(userId);
            return deleteBlogRequest;
        }
    }
}
