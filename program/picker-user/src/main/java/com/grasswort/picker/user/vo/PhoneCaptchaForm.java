package com.grasswort.picker.user.vo;

import com.grasswort.picker.user.validator.Mobile;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author xuliangliang
 * @Classname PhoneCaptchaForm
 * @Description 获取手机验证码
 * @Date 2019/11/4 11:36
 * @blame Java Team
 */
@Data
public class PhoneCaptchaForm {
    @Mobile
    @NotNull
    private String phone;
}
