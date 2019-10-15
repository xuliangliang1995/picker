package com.grasswort.picker.user.dto;

import com.grasswort.picker.commons.result.AbstractRequest;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author xuliangliang
 * @Classname CheckAuthRequest
 * @Description TODO
 * @Date 2019/9/21 17:03
 * @blame Java Team
 */
@Data
public class CheckAuthRequest extends AbstractRequest {
    @NotEmpty
    private String token;
    @NotEmpty
    private String ip;

    @Override
    public void requestCheck() {

    }


    public static final class Builder {
        private String token;
        private String ip;

        private Builder() {
        }

        public static Builder aCheckAuthRequest() {
            return new Builder();
        }

        public Builder withToken(String token) {
            this.token = token;
            return this;
        }

        public Builder withIp(String ip) {
            this.ip = ip;
            return this;
        }

        public CheckAuthRequest build() {
            CheckAuthRequest checkAuthRequest = new CheckAuthRequest();
            checkAuthRequest.setToken(token);
            checkAuthRequest.setIp(ip);
            return checkAuthRequest;
        }
    }
}
