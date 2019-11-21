package com.grasswort.picker.user.dto;

import com.grasswort.picker.commons.result.AbstractRequest;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author xuliangliang
 * @Classname SubscribeRequest
 * @Description 关注
 * @Date 2019/11/21 14:50
 * @blame Java Team
 */
@Data
public class SubscribeRequest extends AbstractRequest {
    @Min(1)
    @NotNull
    private Long userId;
    @NotNull
    @Min(1)
    private Long authorId;

    @Override
    public void requestCheck() {

    }

    public static final class Builder {
        private Long userId;
        private Long authorId;

        private Builder() {
        }

        public static Builder aSubscribeRequest() {
            return new Builder();
        }

        public Builder withUserId(Long userId) {
            this.userId = userId;
            return this;
        }

        public Builder withAuthorId(Long authorId) {
            this.authorId = authorId;
            return this;
        }

        public SubscribeRequest build() {
            SubscribeRequest subscribeRequest = new SubscribeRequest();
            subscribeRequest.setUserId(userId);
            subscribeRequest.setAuthorId(authorId);
            return subscribeRequest;
        }
    }
}
