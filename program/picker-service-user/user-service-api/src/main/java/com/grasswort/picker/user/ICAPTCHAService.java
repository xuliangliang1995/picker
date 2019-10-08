package com.grasswort.picker.user;

import com.grasswort.picker.user.dto.CAPTCHARequest;
import com.grasswort.picker.user.dto.CAPTCHAResponse;

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
    CAPTCHAResponse sendCAPCHA(CAPTCHARequest captchaRequest);

}
