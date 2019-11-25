package com.grasswort.picker.blog.dto;

import com.grasswort.picker.commons.result.AbstractRequest;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author xuliangliang
 * @Classname BlogUnlikeRequest
 * @Description 点赞取消
 * @Date 2019/11/25 14:19
 * @blame Java Team
 */
@Data
public class BlogLikeCancelRequest extends AbstractRequest {

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

        public static Builder aBlogLikeCancelRequest() {
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

        public BlogLikeCancelRequest build() {
            BlogLikeCancelRequest blogLikeCancelRequest = new BlogLikeCancelRequest();
            blogLikeCancelRequest.setBlogId(blogId);
            blogLikeCancelRequest.setUserId(userId);
            return blogLikeCancelRequest;
        }
    }
}
