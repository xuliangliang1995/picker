package com.grasswort.picker.user;

import com.grasswort.picker.user.dto.CAPTCHARequest;
import com.grasswort.picker.user.dto.CAPTCHAResponse;
import com.grasswort.picker.user.dto.PhoneCaptchaRequest;
import com.grasswort.picker.user.dto.PhoneCaptchaResponse;
import org.springframework.validation.annotation.Validated;

/**
 * @author xuliangliang
 * @Classname ICAPTCHAService
 * @Description 验证码服务
 * @Date 2019/10/8 23:04
 * @blame Java Team
 */
public interface ICAPTCHAService {
    /**
     * 发送验证码
     * @param captchaRequest
     * @return
     */
    CAPTCHAResponse sendCAPCHA(@Validated CAPTCHARequest captchaRequest);

    /**
     * 给某个手机号发送验证码
     * @param request
     * @return
     */
    PhoneCaptchaResponse sendCaptchaToPhone(@Validated PhoneCaptchaRequest request);
}
