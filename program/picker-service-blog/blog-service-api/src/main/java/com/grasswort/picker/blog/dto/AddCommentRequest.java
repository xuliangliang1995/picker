package com.grasswort.picker.blog.dto;

import com.grasswort.picker.commons.result.AbstractRequest;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author xuliangliang
 * @Classname AddCommentRequest
 * @Description 评论
 * @Date 2019/11/21 20:05
 * @blame Java Team
 */
@Data
public class AddCommentRequest extends AbstractRequest {
    @NotNull
    @Min(1)
    private String blogId;
    @NotNull
    @Min(0)
    private Long replyCommentId;
    @NotNull
    @Min(1)
    private Long userId;
    @NotEmpty
    private String content;



    @Override
    public void requestCheck() {

    }

    public static final class Builder {
        private String blogId;
        private Long replyCommentId;
        private Long userId;
        private String content;

        private Builder() {
        }

        public static Builder anAddCommentRequest() {
            return new Builder();
        }

        public Builder withBlogId(String blogId) {
            this.blogId = blogId;
            return this;
        }

        public Builder withReplyCommentId(Long replyCommentId) {
            this.replyCommentId = replyCommentId;
            return this;
        }

        public Builder withUserId(Long userId) {
            this.userId = userId;
            return this;
        }

        public Builder withContent(String content) {
            this.content = content;
            return this;
        }

        public AddCommentRequest build() {
            AddCommentRequest addCommentRequest = new AddCommentRequest();
            addCommentRequest.setBlogId(blogId);
            addCommentRequest.setReplyCommentId(replyCommentId);
            addCommentRequest.setUserId(userId);
            addCommentRequest.setContent(content);
            return addCommentRequest;
        }
    }
}
