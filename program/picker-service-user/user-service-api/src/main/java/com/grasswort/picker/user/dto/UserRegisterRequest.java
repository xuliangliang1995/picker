package com.grasswort.picker.user.dto;

import com.grasswort.picker.commons.result.AbstractRequest;
import com.grasswort.picker.user.validator.Password;
import com.grasswort.picker.user.validator.Username;
import lombok.Data;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;

/**
 * @author xuliangliang
 * @Classname UserRegisterRequest
 * @Description TODO
 * @Date 2019/9/21 17:07
 * @blame Java Team
 */
@Data
public class UserRegisterRequest extends AbstractRequest {
    @NotNull
    @Username
    private String username;
    @NotNull
    @Password
    private String password;
    @NotNull
    @Email
    private String email;

    @Override
    public void requestCheck() {
        System.out.println("执行参数校验！");
    }


    public static final class Builder {
        private String username;
        private String password;
        private String email;

        private Builder() {
        }

        public static Builder anUserRegisterRequest() {
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

        public Builder withEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder but() {
            return anUserRegisterRequest().withUsername(username).withPassword(password).withEmail(email);
        }

        public UserRegisterRequest build() {
            UserRegisterRequest userRegisterRequest = new UserRegisterRequest();
            userRegisterRequest.setUsername(username);
            userRegisterRequest.setPassword(password);
            userRegisterRequest.setEmail(email);
            return userRegisterRequest;
        }
    }
}
