package com.grasswort.picker.blog.dto;

import com.grasswort.picker.commons.result.AbstractRequest;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author xuliangliang
 * @Classname BlogLikeRequest
 * @Description 点赞
 * @Date 2019/11/25 13:58
 * @blame Java Team
 */
@Data
public class BlogLikeRequest extends AbstractRequest {
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

        public static Builder aBlogLikeRequest() {
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

        public BlogLikeRequest build() {
            BlogLikeRequest blogLikeRequest = new BlogLikeRequest();
            blogLikeRequest.setBlogId(blogId);
            blogLikeRequest.setUserId(userId);
            return blogLikeRequest;
        }
    }
}
