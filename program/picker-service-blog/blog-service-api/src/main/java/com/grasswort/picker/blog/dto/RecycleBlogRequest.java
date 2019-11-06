package com.grasswort.picker.blog.dto;

import com.grasswort.picker.commons.result.AbstractRequest;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author xuliangliang
 * @Classname RecycleBlogRequest
 * @Description 回收博客
 * @Date 2019/11/6 22:31
 * @blame Java Team
 */
@Data
public class RecycleBlogRequest extends AbstractRequest {
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

        public static Builder aRecycleBlogRequest() {
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

        public RecycleBlogRequest build() {
            RecycleBlogRequest recycleBlogRequest = new RecycleBlogRequest();
            recycleBlogRequest.setBlogId(blogId);
            recycleBlogRequest.setUserId(userId);
            return recycleBlogRequest;
        }
    }
}
