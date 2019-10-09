package com.grasswort.picker.user.controller;

import com.grasswort.picker.commons.result.ResponseData;
import com.grasswort.picker.commons.result.ResponseUtil;
import com.grasswort.picker.user.ICAPTCHAService;
import com.grasswort.picker.user.constants.CAPTCHAReceiver;
import com.grasswort.picker.user.constants.SysRetCodeConstants;
import com.grasswort.picker.user.dto.CAPTCHARequest;
import com.grasswort.picker.user.dto.CAPTCHAResponse;
import com.grasswort.picker.user.model.PickerInfoHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xuliangliang
 * @Classname CaptchaController
 * @Description 验证码
 * @Date 2019/10/8 23:36
 * @blame Java Team
 */
@Api(tags = "Picker 获取验证码")
@RestController
@RequestMapping("/user/captcha")
public class CaptchaController {

    @Reference(version = "1.0")
    ICAPTCHAService icaptchaService;

    @ApiOperation(value = "登录状态发送验证码到邮箱")
    @GetMapping("/email")
    public ResponseData getEmailCaptcha() {
        CAPTCHARequest captchaRequest = new CAPTCHARequest();
        captchaRequest.setReceiver(CAPTCHAReceiver.EMAIL);
        captchaRequest.setUserId(PickerInfoHolder.getPickerInfo().getId());
        CAPTCHAResponse captchaResponse = icaptchaService.sendCAPCHA(captchaRequest);
        if (SysRetCodeConstants.SUCCESS.getCode().equals(captchaResponse.getCode())) {
            return new ResponseUtil<>().setData(null, "发送成功");
        }
        return new ResponseUtil<>().setErrorMsg(captchaResponse.getMsg());
    }
}
