package com.grasswort.picker.blog.dto;

import com.grasswort.picker.commons.result.AbstractRequest;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author xuliangliang
 * @Classname BlogFavoriteCancelRequest
 * @Description 取消收藏
 * @Date 2019/11/25 14:28
 * @blame Java Team
 */
@Data
public class BlogFavoriteCancelRequest extends AbstractRequest {
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

        public static Builder aBlogFavoriteCancelRequest() {
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

        public BlogFavoriteCancelRequest build() {
            BlogFavoriteCancelRequest blogFavoriteCancelRequest = new BlogFavoriteCancelRequest();
            blogFavoriteCancelRequest.setBlogId(blogId);
            blogFavoriteCancelRequest.setUserId(userId);
            return blogFavoriteCancelRequest;
        }
    }
}
