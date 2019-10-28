package com.grasswort.picker.user.dto;

import com.grasswort.picker.commons.result.AbstractRequest;
import com.grasswort.picker.user.validator.Username;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author xuliangliang
 * @Classname SendActivateEmailRequest
 * @Description TODO
 * @Date 2019/10/8 16:11
 * @blame Java Team
 */
@Data
public class SendActivateEmailRequest extends AbstractRequest {
    @NotNull
    @Username
    private String username;
    @Override
    public void requestCheck() {

    }


    public static final class Builder {
        private String username;

        private Builder() {
        }

        public static Builder aSendActivateEmailRequest() {
            return new Builder();
        }

        public Builder withUsername(String username) {
            this.username = username;
            return this;
        }

        public SendActivateEmailRequest build() {
            SendActivateEmailRequest sendActivateEmailRequest = new SendActivateEmailRequest();
            sendActivateEmailRequest.setUsername(username);
            return sendActivateEmailRequest;
        }
    }
}
