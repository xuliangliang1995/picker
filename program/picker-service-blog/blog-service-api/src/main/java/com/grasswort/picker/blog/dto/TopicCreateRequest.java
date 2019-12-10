package com.grasswort.picker.blog.dto;

import com.grasswort.picker.commons.result.AbstractRequest;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author xuliangliang
 * @Classname TopicCreateRequest
 * @Description
 * @Date 2019/12/10 15:32
 * @blame Java Team
 */
@Data
public class TopicCreateRequest extends AbstractRequest {
    @NotNull
    @Min(1)
    private Long pkUserId;
    @NotEmpty
    @Size(max = 50)
    private String title;
    @Size(max = 500)
    private String summary;
    @Size(max = 120)
    private String coverImg;
    @Override
    public void requestCheck() {

    }

    public static final class Builder {
        private Long pkUserId;
        private String title;
        private String summary;
        private String coverImg;

        private Builder() {
        }

        public static Builder aTopicCreateRequest() {
            return new Builder();
        }

        public Builder withPkUserId(Long pkUserId) {
            this.pkUserId = pkUserId;
            return this;
        }

        public Builder withTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder withSummary(String summary) {
            this.summary = summary;
            return this;
        }

        public Builder withCoverImg(String coverImg) {
            this.coverImg = coverImg;
            return this;
        }

        public TopicCreateRequest build() {
            TopicCreateRequest topicCreateRequest = new TopicCreateRequest();
            topicCreateRequest.setPkUserId(pkUserId);
            topicCreateRequest.setTitle(title);
            topicCreateRequest.setSummary(summary);
            topicCreateRequest.setCoverImg(coverImg);
            return topicCreateRequest;
        }
    }
}
