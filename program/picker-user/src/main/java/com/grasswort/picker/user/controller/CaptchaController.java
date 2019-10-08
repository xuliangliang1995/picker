package com.grasswort.picker.user.controller;

import com.grasswort.picker.commons.result.ResponseData;
import com.grasswort.picker.commons.result.ResponseUtil;
import com.grasswort.picker.user.ICAPTCHAService;
import com.grasswort.picker.user.constants.CAPTCHAReceiver;
import com.grasswort.picker.user.constants.SysRetCodeConstants;
import com.grasswort.picker.user.dto.CAPTCHARequest;
import com.grasswort.picker.user.dto.CAPTCHAResponse;
import com.grasswort.picker.user.model.PickInfoHolder;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xuliangliang
 * @Classname CapchaController
 * @Description 验证码
 * @Date 2019/10/8 23:36
 * @blame Java Team
 */
@RestController
@RequestMapping("/user/captcha")
public class CaptchaController {

    @Reference(version = "1.0")
    ICAPTCHAService icaptchaService;

    @GetMapping("/email")
    public ResponseData getEmailCaptcha() {
        CAPTCHARequest captchaRequest = new CAPTCHARequest();
        captchaRequest.setReceiver(CAPTCHAReceiver.EMAIL);
        captchaRequest.setUserId(PickInfoHolder.getPickerInfo().getId());
        CAPTCHAResponse captchaResponse = icaptchaService.sendCAPCHA(captchaRequest);
        if (SysRetCodeConstants.SUCCESS.getCode().equals(captchaResponse.getCode())) {
            return new ResponseUtil<>().setData(null, "发送成功");
        }
        return new ResponseUtil<>().setErrorMsg(captchaResponse.getMsg());
    }
}
