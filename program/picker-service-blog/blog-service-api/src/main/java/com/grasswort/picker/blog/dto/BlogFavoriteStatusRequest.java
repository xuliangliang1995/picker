package com.grasswort.picker.blog.dto;

import com.grasswort.picker.commons.result.AbstractRequest;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author xuliangliang
 * @Classname BlogFavoriteStatusRequest
 * @Description 收藏状态
 * @Date 2019/11/25 14:57
 * @blame Java Team
 */
@Data
public class BlogFavoriteStatusRequest extends AbstractRequest {
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

        public static Builder aBlogFavoriteStatusRequest() {
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

        public BlogFavoriteStatusRequest build() {
            BlogFavoriteStatusRequest blogFavoriteStatusRequest = new BlogFavoriteStatusRequest();
            blogFavoriteStatusRequest.setBlogId(blogId);
            blogFavoriteStatusRequest.setUserId(userId);
            return blogFavoriteStatusRequest;
        }
    }
}
