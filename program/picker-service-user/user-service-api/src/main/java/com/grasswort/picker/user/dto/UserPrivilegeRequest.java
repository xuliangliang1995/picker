package com.grasswort.picker.user.dto;

import com.grasswort.picker.commons.result.AbstractRequest;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * @author xuliangliang
 * @Classname UserPrivilegeRequest
 * @Description 提高权限
 * @Date 2019/10/10 20:01
 * @blame Java Team
 */
@Data
public class UserPrivilegeRequest extends AbstractRequest {
    /**
     * ip 地址（提权必须提供 IP 地址）
     */
    @NotEmpty
    private String ip;
    /**
     * 用户 id
     */
    @NotNull
    private Long userId;
    /**
     * 验证码（发往用户注册手机或邮箱的验证码）
     */
    @NotEmpty
    private String captch;

    @Override
    public void requestCheck() {

    }


    public static final class Builder {
        private String ip;
        private Long userId;
        private String captch;

        private Builder() {
        }

        public static Builder anUserPrivilegeRequest() {
            return new Builder();
        }

        public Builder withIp(String ip) {
            this.ip = ip;
            return this;
        }

        public Builder withUserId(Long userId) {
            this.userId = userId;
            return this;
        }

        public Builder withCaptch(String captch) {
            this.captch = captch;
            return this;
        }

        public UserPrivilegeRequest build() {
            UserPrivilegeRequest userPrivilegeRequest = new UserPrivilegeRequest();
            userPrivilegeRequest.setIp(ip);
            userPrivilegeRequest.setUserId(userId);
            userPrivilegeRequest.setCaptch(captch);
            return userPrivilegeRequest;
        }
    }
}
