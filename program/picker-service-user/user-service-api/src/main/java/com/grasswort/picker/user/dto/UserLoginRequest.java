package com.grasswort.picker.user.dto;

import com.grasswort.picker.commons.result.AbstractRequest;
import com.grasswort.picker.user.validator.Password;
import com.grasswort.picker.user.validator.Username;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author xuliangliang
 * @Classname UserLoginRequest
 * @Description 登录
 * @Date 2019/9/21 16:59
 * @blame Java Team
 */
@Data
public class UserLoginRequest extends AbstractRequest {
    @NotNull
    @Username
    private String username;
    @NotNull
    @Password
    private String password;

    @Override
    public void requestCheck() {

    }


    public static final class Builder {
        private String username;
        private String password;

        private Builder() {
        }

        public static Builder anUserLoginRequest() {
            return new Builder();
        }

        public Builder withUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder withPassword(String password) {
            this.password = password;
            return this;
        }

        public UserLoginRequest build() {
            UserLoginRequest userLoginRequest = new UserLoginRequest();
            userLoginRequest.setUsername(username);
            userLoginRequest.setPassword(password);
            return userLoginRequest;
        }
    }
}
