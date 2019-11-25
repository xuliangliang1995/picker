package com.grasswort.picker.blog.dto;

import com.grasswort.picker.commons.result.AbstractRequest;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author xuliangliang
 * @Classname BlogBrowseRequest
 * @Description 博客浏览记录
 * @Date 2019/11/25 16:56
 * @blame Java Team
 */
@Data
public class BlogBrowseRequest extends AbstractRequest {
    @NotEmpty
    private String blogId;
    @Override
    public void requestCheck() {

    }

    public static final class Builder {
        private String blogId;

        private Builder() {
        }

        public static Builder aBlogBrowseRequest() {
            return new Builder();
        }

        public Builder withBlogId(String blogId) {
            this.blogId = blogId;
            return this;
        }

        public BlogBrowseRequest build() {
            BlogBrowseRequest blogBrowseRequest = new BlogBrowseRequest();
            blogBrowseRequest.setBlogId(blogId);
            return blogBrowseRequest;
        }
    }
}
