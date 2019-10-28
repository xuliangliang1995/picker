package com.grasswort.picker.blog.dto;

import com.grasswort.picker.commons.result.AbstractRequest;
import lombok.Data;

/**
 * @author xuliangliang
 * @Classname CreateBlogCategoryRequest
 * @Description 创建博客分类
 * @Date 2019/10/21 9:57
 * @blame Java Team
 */
@Data
public class CreateBlogCategoryRequest extends AbstractRequest {

    private Long userId;

    private String category;

    @Override
    public void requestCheck() {

    }


    public static final class Builder {
        private Long userId;
        private String category;

        private Builder() {
        }

        public static Builder aCreateBlogCategoryRequest() {
            return new Builder();
        }

        public Builder withUserId(Long userId) {
            this.userId = userId;
            return this;
        }

        public Builder withCategory(String category) {
            this.category = category;
            return this;
        }

        public CreateBlogCategoryRequest build() {
            CreateBlogCategoryRequest createBlogCategoryRequest = new CreateBlogCategoryRequest();
            createBlogCategoryRequest.setUserId(userId);
            createBlogCategoryRequest.setCategory(category);
            return createBlogCategoryRequest;
        }
    }
}
