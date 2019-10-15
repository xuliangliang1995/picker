package com.grasswort.picker.user.dto;

import com.grasswort.picker.commons.result.AbstractRequest;
import com.grasswort.picker.user.validator.Username;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author xuliangliang
 * @Classname UserActivateRequest
 * @Description TODO
 * @Date 2019/10/6 22:31
 * @blame Java Team
 */
@Data
public class UserActivateRequest extends AbstractRequest {
    @NotNull
    @Username
    private String username;
    @NotNull
    @Size(min = 32, max = 32)
    private String activationCode;
    @NotNull
    private Long activateId;

    @Override
    public void requestCheck() {

    }


    public static final class Builder {
        private String username;
        private String activationCode;
        private Long activateId;

        private Builder() {
        }

        public static Builder anUserActivateRequest() {
            return new Builder();
        }

        public Builder withUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder withActivationCode(String activationCode) {
            this.activationCode = activationCode;
            return this;
        }

        public Builder withActivateId(Long activateId) {
            this.activateId = activateId;
            return this;
        }

        public UserActivateRequest build() {
            UserActivateRequest userActivateRequest = new UserActivateRequest();
            userActivateRequest.setUsername(username);
            userActivateRequest.setActivationCode(activationCode);
            userActivateRequest.setActivateId(activateId);
            return userActivateRequest;
        }
    }
}
