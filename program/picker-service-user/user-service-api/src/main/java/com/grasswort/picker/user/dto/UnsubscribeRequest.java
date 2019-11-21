package com.grasswort.picker.user.dto;

import com.grasswort.picker.commons.result.AbstractRequest;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author xuliangliang
 * @Classname UnsubscribeRequest
 * @Description 取消关注
 * @Date 2019/11/21 15:07
 * @blame Java Team
 */
@Data
public class UnsubscribeRequest extends AbstractRequest {
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

        public static Builder anUnsubscribeRequest() {
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

        public UnsubscribeRequest build() {
            UnsubscribeRequest unsubscribeRequest = new UnsubscribeRequest();
            unsubscribeRequest.setUserId(userId);
            unsubscribeRequest.setAuthorId(authorId);
            return unsubscribeRequest;
        }
    }
}
