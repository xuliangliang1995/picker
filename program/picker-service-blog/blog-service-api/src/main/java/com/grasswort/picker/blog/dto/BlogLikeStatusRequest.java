package com.grasswort.picker.blog.dto;

import com.grasswort.picker.commons.result.AbstractRequest;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author xuliangliang
 * @Classname BlogLikeStatusRequest
 * @Description 点赞状态
 * @Date 2019/11/25 14:55
 * @blame Java Team
 */
@Data
public class BlogLikeStatusRequest extends AbstractRequest {
    @NotEmpty
    private String blogId;

    @Min(1)
    @NotNull
    private Long userId;

    @Override
    public void requestCheck() {

    }

    public static final class Builder {
        private String blogId;
        private Long userId;

        private Builder() {
        }

        public static Builder aBlogLikeStatusRequest() {
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

        public BlogLikeStatusRequest build() {
            BlogLikeStatusRequest blogLikeStatusRequest = new BlogLikeStatusRequest();
            blogLikeStatusRequest.setBlogId(blogId);
            blogLikeStatusRequest.setUserId(userId);
            return blogLikeStatusRequest;
        }
    }
}
