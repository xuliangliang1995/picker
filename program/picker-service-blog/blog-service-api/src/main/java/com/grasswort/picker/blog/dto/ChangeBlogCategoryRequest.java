package com.grasswort.picker.blog.dto;

import com.grasswort.picker.commons.result.AbstractRequest;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author xuliangliang
 * @Classname ChangeBlogCategoryRequest
 * @Description 修改博客分类
 * @Date 2019/11/6 10:12
 * @blame Java Team
 */
@Data
public class ChangeBlogCategoryRequest extends AbstractRequest {
    @NotNull
    @Min(1)
    private Long userId;
    @NotEmpty
    private String blogId;
    @NotNull
    @Min(0)
    private Long categoryId;

    @Override
    public void requestCheck() {

    }

    public static final class Builder {
        private Long userId;
        private String blogId;
        private Long categoryId;

        private Builder() {
        }

        public static Builder aChangeBlogCategoryRequest() {
            return new Builder();
        }

        public Builder withUserId(Long userId) {
            this.userId = userId;
            return this;
        }

        public Builder withBlogId(String blogId) {
            this.blogId = blogId;
            return this;
        }

        public Builder withCategoryId(Long categoryId) {
            this.categoryId = categoryId;
            return this;
        }

        public ChangeBlogCategoryRequest build() {
            ChangeBlogCategoryRequest changeBlogCategoryRequest = new ChangeBlogCategoryRequest();
            changeBlogCategoryRequest.setUserId(userId);
            changeBlogCategoryRequest.setBlogId(blogId);
            changeBlogCategoryRequest.setCategoryId(categoryId);
            return changeBlogCategoryRequest;
        }
    }
}
