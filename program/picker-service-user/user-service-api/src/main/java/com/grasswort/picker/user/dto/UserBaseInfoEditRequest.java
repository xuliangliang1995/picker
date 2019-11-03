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
    @Mobile
    private String phone;

    private String captcha;

    @NotNull
    private Byte sex;
    @Size(max = 120)
    private String avatar;

    @Override
    public void requestCheck() {

    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public Byte getSex() {
        return sex;
    }

    public void setSex(Byte sex) {
        this.sex = sex;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
