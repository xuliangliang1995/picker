package com.grasswort.picker.user.dto;

import com.grasswort.picker.commons.result.AbstractRequest;
import com.grasswort.picker.user.validator.Password;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author xuliangliang
 * @Classname UserChangePwdRequest
 * @Description 修改密码
 * @Date 2019/10/12 16:36
 * @blame Java Team
 */
@Data
public class UserChangePwdRequest extends AbstractRequest {
    @NotNull
    @Password
    private String password;

    @NotEmpty
    private String ip;

    @NotEmpty
    private String accessToken;

    @Override
    public void requestCheck() {

    }


    public static final class Builder {
        private String password;
        private String ip;
        private String accessToken;

        private Builder() {
        }

        public static Builder anUserChangePwdRequest() {
            return new Builder();
        }

        public Builder withPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder withIp(String ip) {
            this.ip = ip;
            return this;
        }

        public Builder withAccessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public UserChangePwdRequest build() {
            UserChangePwdRequest userChangePwdRequest = new UserChangePwdRequest();
            userChangePwdRequest.setPassword(password);
            userChangePwdRequest.setIp(ip);
            userChangePwdRequest.setAccessToken(accessToken);
            return userChangePwdRequest;
        }
    }
}
