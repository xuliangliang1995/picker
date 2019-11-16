package com.grasswort.picker.user.controller;

import com.grasswort.picker.commons.result.ResponseData;
import com.grasswort.picker.commons.result.ResponseUtil;
import com.grasswort.picker.commons.validator.ValidatorTool;
import com.grasswort.picker.user.ICAPTCHAService;
import com.grasswort.picker.user.annotation.Anoymous;
import com.grasswort.picker.user.constants.CAPTCHAReceiver;
import com.grasswort.picker.user.dto.CAPTCHARequest;
import com.grasswort.picker.user.dto.CAPTCHAResponse;
import com.grasswort.picker.user.dto.PhoneCaptchaRequest;
import com.grasswort.picker.user.dto.PhoneCaptchaResponse;
import com.grasswort.picker.user.model.PickerInfoHolder;
import com.grasswort.picker.user.vo.PhoneCaptchaForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * @author xuliangliang
 * @Classname CaptchaController
 * @Description 验证码
 * @Date 2019/10/8 23:36
 * @blame Java Team
 */
@Api(tags = "Picker 获取验证码")
@RestController
@RequestMapping("/captcha")
public class CaptchaController {

    @Reference(version = "1.0", timeout = 10000)
    ICAPTCHAService icaptchaService;

    @ApiOperation(value = "登录状态发送验证码到邮箱")
    @GetMapping("/email")
    public ResponseData getEmailCaptcha() {
        CAPTCHARequest captchaRequest = CAPTCHARequest.Builder.aCAPTCHARequest()
                .withReceiver(CAPTCHAReceiver.EMAIL)
                .withUserId(PickerInfoHolder.getPickerInfo().getId())
                .build();

        CAPTCHAResponse captchaResponse = icaptchaService.sendCAPCHA(captchaRequest);

        return Optional.ofNullable(captchaResponse)
                .map(r -> r.isSuccess()
                        ? new ResponseUtil<>().setData(captchaResponse.getEmail())
                        : new ResponseUtil<>().setErrorMsg(captchaResponse.getMsg())
                )
                .orElse(ResponseData.SYSTEM_ERROR);
    }

    @ApiOperation(value = "登录状态发送验证码到手机")
    @GetMapping("/sms")
    public ResponseData getSmsCaptcha() {
        CAPTCHARequest captchaRequest = CAPTCHARequest.Builder.aCAPTCHARequest()
                .withReceiver(CAPTCHAReceiver.PHONE)
                .withUserId(PickerInfoHolder.getPickerInfo().getId())
                .build();

        CAPTCHAResponse captchaResponse = icaptchaService.sendCAPCHA(captchaRequest);

        return Optional.ofNullable(captchaResponse)
                .map(r -> r.isSuccess()
                        ? new ResponseUtil<>().setData(captchaResponse.getPhone())
                        : new ResponseUtil<>().setErrorMsg(captchaResponse.getMsg())
                )
                .orElse(ResponseData.SYSTEM_ERROR);
    }

    @ApiOperation(value = "登录状态发送验证码到微信")
    @GetMapping("/wechat")
    public ResponseData getWechatCaptcha() {
        CAPTCHARequest captchaRequest = CAPTCHARequest.Builder.aCAPTCHARequest()
                .withReceiver(CAPTCHAReceiver.WECHAT)
                .withUserId(PickerInfoHolder.getPickerInfo().getId())
                .build();

        CAPTCHAResponse captchaResponse = icaptchaService.sendCAPCHA(captchaRequest);

        return Optional.ofNullable(captchaResponse)
                .map(r -> r.isSuccess()
                        ? new ResponseUtil<>().setData(captchaResponse.getMpNickName())
                        : new ResponseUtil<>().setErrorMsg(captchaResponse.getMsg())
                )
                .orElse(ResponseData.SYSTEM_ERROR);
    }

    @Anoymous
    @ApiOperation(value = "获取手机号验证码（不需要登录）")
    @PostMapping("/phone")
    public ResponseData getPhoneCaptcha(@RequestBody @Validated PhoneCaptchaForm form, BindingResult bindingResult) {
        ValidatorTool.check(bindingResult);

        PhoneCaptchaRequest captchaRequest = new PhoneCaptchaRequest(form.getPhone());
        PhoneCaptchaResponse captchaResponse = icaptchaService.sendCaptchaToPhone(captchaRequest);

        return Optional.ofNullable(captchaResponse)
                .map(r -> r.isSuccess()
                        ? new ResponseUtil<>().setData(null)
                        : new ResponseUtil<>().setErrorMsg(captchaResponse.getMsg())
                )
                .orElse(ResponseData.SYSTEM_ERROR);
    }
}
