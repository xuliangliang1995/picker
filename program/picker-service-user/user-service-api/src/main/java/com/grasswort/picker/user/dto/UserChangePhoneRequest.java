package com.grasswort.picker.user.dto;

import com.grasswort.picker.commons.result.AbstractRequest;
import com.grasswort.picker.user.validator.Mobile;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author xuliangliang
 * @Classname ChangePhoneRequest
 * @Description 修改手机号
 * @Date 2019/11/3 19:57
 * @blame Java Team
 */
@Data
public class UserChangePhoneRequest extends AbstractRequest {
    @NotEmpty
    private String accessToken;
    @NotEmpty
    private String ip;
    @Mobile
    private String phone;
    @NotEmpty
    @Size(min = 6, max = 6)
    private String captcha;

    @Override
    public void requestCheck() {

    }

    public static final class Builder {
        private String accessToken;
        private String ip;
        private String phone;
        private String captcha;

        private Builder() {
        }

        public static Builder anUserChangePhoneRequest() {
            return new Builder();
        }

        public Builder withAccessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public Builder withIp(String ip) {
            this.ip = ip;
            return this;
        }

        public Builder withPhone(String phone) {
            this.phone = phone;
            return this;
        }

        public Builder withCaptcha(String captcha) {
            this.captcha = captcha;
            return this;
        }

        public UserChangePhoneRequest build() {
            UserChangePhoneRequest userChangePhoneRequest = new UserChangePhoneRequest();
            userChangePhoneRequest.setAccessToken(accessToken);
            userChangePhoneRequest.setIp(ip);
            userChangePhoneRequest.setPhone(phone);
            userChangePhoneRequest.setCaptcha(captcha);
            return userChangePhoneRequest;
        }
    }
}
