package com.grasswort.picker.blog.dto;

import com.grasswort.picker.commons.result.AbstractRequest;
import lombok.Data;

import javax.validation.constraints.*;

/**
 * @author xuliangliang
 * @Classname TopicMenuCreateRequest
 * @Description 创建菜单请求
 * @Date 2019/12/11 14:03
 * @blame Java Team
 */
@Data
public class TopicMenuCreateRequest extends AbstractRequest {
    @NotEmpty
    private String topicId;
    @NotNull
    @Min(1)
    private Long pkUserId;
    @NotNull
    @Min(1)
    private Long parentMenuId;
    @NotEmpty
    @Size(max = 50)
    private String name;
    @NotNull
    @Min(1)
    @Max(3)
    private Integer type;

    private String blogId;

    @Override
    public void requestCheck() {

    }


    public static final class Builder {
        private String topicId;
        private Long pkUserId;
        private Long parentMenuId;
        private String name;
        private Integer type;
        private String blogId;

        private Builder() {
        }

        public static Builder aTopicMenuCreateRequest() {
            return new Builder();
        }

        public Builder withTopicId(String topicId) {
            this.topicId = topicId;
            return this;
        }

        public Builder withPkUserId(Long pkUserId) {
            this.pkUserId = pkUserId;
            return this;
        }

        public Builder withParentMenuId(Long parentMenuId) {
            this.parentMenuId = parentMenuId;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withType(Integer type) {
            this.type = type;
            return this;
        }

        public Builder withBlogId(String blogId) {
            this.blogId = blogId;
            return this;
        }

        public TopicMenuCreateRequest build() {
            TopicMenuCreateRequest topicMenuCreateRequest = new TopicMenuCreateRequest();
            topicMenuCreateRequest.setTopicId(topicId);
            topicMenuCreateRequest.setPkUserId(pkUserId);
            topicMenuCreateRequest.setParentMenuId(parentMenuId);
            topicMenuCreateRequest.setName(name);
            topicMenuCreateRequest.setType(type);
            topicMenuCreateRequest.setBlogId(blogId);
            return topicMenuCreateRequest;
        }
    }
}
