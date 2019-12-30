package com.grasswort.picker.blog.dto;

import com.grasswort.picker.blog.dto.topic.TopicItem;
import lombok.Data;

import java.util.List;

/**
 * @author xuliangliang
 * @Classname TopicFavoriteListResponse
 * @Description
 * @Date 2019/12/30
 * @blame Java Team
 */
@Data
public class TopicFavoriteListResponse extends AbstractBlogResponse {

    private List<TopicItem> topics;

    private Long total;

    public static final class Builder {
        private List<TopicItem> topics;
        private Long total;
        private String code;
        private String msg;

        private Builder() {
        }

        public static Builder aTopicFavoriteListResponse() {
            return new Builder();
        }

        public Builder withTopics(List<TopicItem> topics) {
            this.topics = topics;
            return this;
        }

        public Builder withTotal(Long total) {
            this.total = total;
            return this;
        }

        public Builder withCode(String code) {
            this.code = code;
            return this;
        }

        public Builder withMsg(String msg) {
            this.msg = msg;
            return this;
        }

        public TopicFavoriteListResponse build() {
            TopicFavoriteListResponse topicFavoriteListResponse = new TopicFavoriteListResponse();
            topicFavoriteListResponse.setTopics(topics);
            topicFavoriteListResponse.setTotal(total);
            topicFavoriteListResponse.setCode(code);
            topicFavoriteListResponse.setMsg(msg);
            return topicFavoriteListResponse;
        }
    }

}
