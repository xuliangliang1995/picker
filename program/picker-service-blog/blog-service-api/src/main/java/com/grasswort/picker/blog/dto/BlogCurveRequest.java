package com.grasswort.picker.blog.dto;

import com.grasswort.picker.blog.constant.BlogCurveStatusEnum;
import com.grasswort.picker.commons.result.AbstractRequest;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author xuliangliang
 * @Classname BlogCurveRequest
 * @Description 修改博客曲线状态
 * @Date 2019/11/10 12:38
 * @blame Java Team
 */
@Data
public class BlogCurveRequest extends AbstractRequest {
    @NotNull
    @Min(1)
    private Long userId;
    @NotEmpty
    private String blogId;
    @Min(0)
    private Integer order;
    @NotNull
    private BlogCurveStatusEnum status;

    @Override
    public void requestCheck() {

    }

    public static final class Builder {
        private Long userId;
        private String blogId;
        private Integer order;
        private BlogCurveStatusEnum status;

        private Builder() {
        }

        public static Builder aBlogCurveRequest() {
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

        public Builder withOrder(Integer order) {
            this.order = order;
            return this;
        }

        public Builder withStatus(BlogCurveStatusEnum status) {
            this.status = status;
            return this;
        }

        public BlogCurveRequest build() {
            BlogCurveRequest blogCurveRequest = new BlogCurveRequest();
            blogCurveRequest.setUserId(userId);
            blogCurveRequest.setBlogId(blogId);
            blogCurveRequest.setOrder(order);
            blogCurveRequest.setStatus(status);
            return blogCurveRequest;
        }
    }
}
