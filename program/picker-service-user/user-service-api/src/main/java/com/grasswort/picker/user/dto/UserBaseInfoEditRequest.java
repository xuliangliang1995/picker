package com.grasswort.picker.user.dto;

import com.grasswort.picker.commons.result.AbstractRequest;
import com.grasswort.picker.user.validator.Mobile;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author xuliangliang
 * @Classname UserBaseInfoEditRequest
 * @Description 编辑用户基本信息
 * @Date 2019/10/9 12:40
 * @blame Java Team
 */
@Data
public class UserBaseInfoEditRequest extends AbstractRequest {
    private Long userId;
    @NotNull
    @Size(min = 2, max = 10)
    private String name;
    @NotNull
    private Byte sex;
    @Size(max = 120)
    private String avatar;

    @Override
    public void requestCheck() {

    }


    public static final class Builder {
        private Long userId;
        private String name;
        private Byte sex;
        private String avatar;

        private Builder() {
        }

        public static Builder anUserBaseInfoEditRequest() {
            return new Builder();
        }

        public Builder withUserId(Long userId) {
            this.userId = userId;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withSex(Byte sex) {
            this.sex = sex;
            return this;
        }

        public Builder withAvatar(String avatar) {
            this.avatar = avatar;
            return this;
        }

        public UserBaseInfoEditRequest build() {
            UserBaseInfoEditRequest userBaseInfoEditRequest = new UserBaseInfoEditRequest();
            userBaseInfoEditRequest.setUserId(userId);
            userBaseInfoEditRequest.setName(name);
            userBaseInfoEditRequest.setSex(sex);
            userBaseInfoEditRequest.setAvatar(avatar);
            return userBaseInfoEditRequest;
        }
    }
}
