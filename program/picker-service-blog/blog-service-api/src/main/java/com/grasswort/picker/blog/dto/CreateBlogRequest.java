package com.grasswort.picker.blog.dto;

import com.grasswort.picker.commons.result.AbstractRequest;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author xuliangliang
 * @Classname CreateBlogRequest
 * @Description 创建 blog
 * @Date 2019/10/19 12:48
 * @blame Java Team
 */
@Data
public class CreateBlogRequest extends AbstractRequest {
    /**
     * 用户 id
     */
    @NotNull
    @Min(1)
    private Long userId;
    /**
     * markdown 内容
     */
    @NotEmpty
    private String markdown;
    /**
     * markdown 内容渲染
     */
    @NotEmpty
    private String html;
    /**
     * 博客分类 ID
     */
    @Min(0)
    private Long categoryId;

    @Override
    public void requestCheck() {

    }


    public static final class Builder {
        private Long userId;
        private String markdown;
        private String html;
        private Long categoryId;

        private Builder() {
        }

        public static Builder aCreateBlogRequest() {
            return new Builder();
        }

        public Builder withUserId(Long userId) {
            this.userId = userId;
            return this;
        }

        public Builder withMarkdown(String markdown) {
            this.markdown = markdown;
            return this;
        }

        public Builder withHtml(String html) {
            this.html = html;
            return this;
        }

        public Builder withCategoryId(Long categoryId) {
            this.categoryId = categoryId;
            return this;
        }

        public CreateBlogRequest build() {
            CreateBlogRequest createBlogRequest = new CreateBlogRequest();
            createBlogRequest.setUserId(userId);
            createBlogRequest.setMarkdown(markdown);
            createBlogRequest.setHtml(html);
            createBlogRequest.setCategoryId(categoryId);
            return createBlogRequest;
        }
    }
}
