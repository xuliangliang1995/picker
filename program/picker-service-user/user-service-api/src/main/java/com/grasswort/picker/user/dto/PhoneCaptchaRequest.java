package com.grasswort.picker.user.dto;

import com.grasswort.picker.commons.result.AbstractRequest;
import com.grasswort.picker.user.validator.Mobile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author xuliangliang
 * @Classname ValidPhoneCaptchaRequest
 * @Description 验证码（检验手机号有效性）
 * @Date 2019/11/4 11:17
 * @blame Java Team
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhoneCaptchaRequest extends AbstractRequest {
    @Mobile
    @NotNull
    private String phone;

    @Override
    public void requestCheck() {

    }

}
