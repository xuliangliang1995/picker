package com.grasswort.picker.blog.dto;

import com.grasswort.picker.commons.result.AbstractRequest;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

/**
 * @author xuliangliang
 * @Classname EditBlogRequest
 * @Description 编辑博客
 * @Date 2019/11/7 22:32
 * @blame Java Team
 */
@Data
public class EditBlogRequest extends AbstractRequest {
    /**
     * 博客 id
     */
    @NotEmpty
    private String blogId;
    /**
     * 用户 id
     */
    @NotNull
    @Min(1)
    private Long userId;
    /**
     * 文章标题
     */
    @NotEmpty
    @Size(min = 1, max = 50)
    private String title;
    /**
     * 博客分类 ID
     */
    @Min(0)
    private Long categoryId;
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
     * 封面配图
     */
    @Size(max = 120)
    private String coverImg;
    /**
     * 内容摘要
     */
    @Size(max = 100)
    private String summary;
    /**
     * 标签（用英文逗号分隔）
     */
    private Set<String> labels;

    @Override
    public void requestCheck() {

    }

    public static final class Builder {
        private String blogId;
        private Long userId;
        private String title;
        private Long categoryId;
        private String markdown;
        private String html;
        private String coverImg;
        private String summary;
        private Set<String> labels;

        private Builder() {
        }

        public static Builder anEditBlogRequest() {
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

        public Builder withTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder withCategoryId(Long categoryId) {
            this.categoryId = categoryId;
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

        public Builder withCoverImg(String coverImg) {
            this.coverImg = coverImg;
            return this;
        }

        public Builder withSummary(String summary) {
            this.summary = summary;
            return this;
        }

        public Builder withLabels(Set<String> labels) {
            this.labels = labels;
            return this;
        }

        public EditBlogRequest build() {
            EditBlogRequest editBlogRequest = new EditBlogRequest();
            editBlogRequest.setBlogId(blogId);
            editBlogRequest.setUserId(userId);
            editBlogRequest.setTitle(title);
            editBlogRequest.setCategoryId(categoryId);
            editBlogRequest.setMarkdown(markdown);
            editBlogRequest.setHtml(html);
            editBlogRequest.setCoverImg(coverImg);
            editBlogRequest.setSummary(summary);
            editBlogRequest.setLabels(labels);
            return editBlogRequest;
        }
    }
}
