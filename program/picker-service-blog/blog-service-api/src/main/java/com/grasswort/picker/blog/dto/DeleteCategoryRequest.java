package com.grasswort.picker.blog.dto;

import com.grasswort.picker.commons.result.AbstractRequest;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author xuliangliang
 * @Classname DeleteCategoryRequest
 * @Description 删除
 * @Date 2019/11/7 15:49
 * @blame Java Team
 */
@Data
public class DeleteCategoryRequest extends AbstractRequest {
    @NotNull
    @Min(1)
    private Long userId;
    @NotNull
    @Min(1)
    private Long categoryId;

    @Override
    public void requestCheck() {

    }

    public static final class Builder {
        private Long userId;
        private Long categoryId;

        private Builder() {
        }

        public static Builder aDeleteCategoryRequest() {
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

        public DeleteCategoryRequest build() {
            DeleteCategoryRequest deleteCategoryRequest = new DeleteCategoryRequest();
            deleteCategoryRequest.setUserId(userId);
            deleteCategoryRequest.setCategoryId(categoryId);
            return deleteCategoryRequest;
        }
    }
}
