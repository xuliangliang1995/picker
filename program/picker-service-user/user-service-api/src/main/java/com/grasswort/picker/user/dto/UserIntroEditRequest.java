package com.grasswort.picker.user.dto;

import com.grasswort.picker.commons.result.AbstractRequest;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author xuliangliang
 * @Classname UserIntroEditRequest.java
 * @Description
 * @Date 2019/12/31
 * @blame Java Team
 */
@Data
public class UserIntroEditRequest extends AbstractRequest {
    @NotNull
    @Min(1)
    private Long userId;
    @NotEmpty
    @Size(max = 500)
    private String intro;

    @Override
    public void requestCheck() {

    }

    public static final class Builder {
        private Long userId;
        private String intro;

        private Builder() {
        }

        public static Builder anUserIntroEditRequest() {
            return new Builder();
        }

        public Builder withUserId(Long userId) {
            this.userId = userId;
            return this;
        }

        public Builder withIntro(String intro) {
            this.intro = intro;
            return this;
        }

        public UserIntroEditRequest build() {
            UserIntroEditRequest userIntroEditRequest = new UserIntroEditRequest();
            userIntroEditRequest.setUserId(userId);
            userIntroEditRequest.setIntro(intro);
            return userIntroEditRequest;
        }
    }
}
