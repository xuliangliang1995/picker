package com.grasswort.picker.blog.dto;

import com.grasswort.picker.commons.result.AbstractRequest;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author xuliangliang
 * @Classname ChangeCategoryParentRequest
 * @Description 修改博客分类
 * @Date 2019/11/6 17:21
 * @blame Java Team
 */
@Data
public class EditCategoryRequest extends AbstractRequest {
    @NotNull
    @Min(1)
    private Long userId;
    @NotNull
    @Min(1)
    private Long categoryId;
    @Min(0)
    private Long parentId;
    @Size(min = 1, max = 20)
    private String category;

    @Override
    public void requestCheck() {

    }

    public static final class Builder {
        private Long userId;
        private Long categoryId;
        private Long parentId;
        private String category;

        private Builder() {
        }

        public static Builder anEditCategoryRequest() {
            return new Builder();
        }

        public Builder withUserId(Long userId) {
            this.userId = userId;
            return this;
        }

        public Builder withCategoryId(Long categoryId) {
            this.categoryId = categoryId;
            return this;
        }

        public Builder withParentId(Long parentId) {
            this.parentId = parentId;
            return this;
        }

        public Builder withCategory(String category) {
            this.category = category;
            return this;
        }

        public EditCategoryRequest build() {
            EditCategoryRequest editCategoryRequest = new EditCategoryRequest();
            editCategoryRequest.setUserId(userId);
            editCategoryRequest.setCategoryId(categoryId);
            editCategoryRequest.setParentId(parentId);
            editCategoryRequest.setCategory(category);
            return editCategoryRequest;
        }
    }
}
