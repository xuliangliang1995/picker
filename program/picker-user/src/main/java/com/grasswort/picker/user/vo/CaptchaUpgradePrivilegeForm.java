package com.grasswort.picker.user.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author xuliangliang
 * @Classname CaptchaUpgradePrivilegeForm
 * @Description 验证码提权表单
 * @Date 2019/10/12 16:05
 * @blame Java Team
 */
@Data
public class CaptchaUpgradePrivilegeForm {
    @NotNull
    @Size(min = 6, max = 6)
    private String captcha;
}
