package com.grasswort.picker.wechat.controller;

import com.grasswort.picker.wechat.IWechatMpService;
import com.grasswort.picker.wechat.constants.SysRetCodeConstants;
import com.grasswort.picker.wechat.dto.WxMpAuthRequest;
import com.grasswort.picker.wechat.dto.WxMpAuthResponse;
import com.grasswort.picker.wechat.dto.WxMpCallbackRequest;
import com.grasswort.picker.wechat.dto.WxMpCallbackResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

/**
 * @author xuliangliang
 * @Classname WxMpController
 * @Description 微信公众号
 * @Date 2019/11/14 15:57
 * @blame Java Team
 */
@RestController
@RequestMapping("/")
public class WxMpController {

    @Reference(version = "1.0", timeout = 10000)
    IWechatMpService iWechatMpService;

    @GetMapping(produces = "text/plain;charset=utf-8")
    public String auGet(@RequestParam(name = "signature", required = false) String signature,
                        @RequestParam(name = "timestamp", required = false) String timestamp,
                        @RequestParam(name = "nonce", required = false) String nonce,
                        @RequestParam(name = "echostr", required = false) String echostr) {
        WxMpAuthRequest authRequest = WxMpAuthRequest.Builder.aWxMpAuthRequest()
                .withSignature(signature)
                .withTimestamp(timestamp)
                .withNonce(nonce)
                .withEchostr(echostr)
                .build();
        WxMpAuthResponse authResponse = iWechatMpService.authGet(authRequest);
        if (SysRetCodeConstants.SUCCESS.equals(authResponse)) {
            return authResponse.getResult();
        }
        return "非法请求";
    }

    @PostMapping(produces = "application/xml; charset=UTF-8")
    public String progress(
            @RequestBody String requestBody,
            @RequestParam("signature") String signature,
            @RequestParam(name = "encrypt_type", required = false) String encType,
            @RequestParam(name = "msg_signature", required = false) String msgSignature,
            @RequestParam("timestamp") String timestamp, @RequestParam("nonce") String nonce) {
        WxMpCallbackResponse callbackResponse = iWechatMpService.processCallback(
                WxMpCallbackRequest.Builder.aWxMpCallbackRequest()
                        .withRequestBody(requestBody)
                        .withSignature(signature)
                        .withEncType(encType)
                        .withMsgSignature(msgSignature)
                        .withTimestamp(timestamp)
                        .withNonce(nonce)
                        .build()
        );
        if (SysRetCodeConstants.SUCCESS.getCode().equals(callbackResponse.getCode())) {
            return callbackResponse.getResult();
        }
        return StringUtils.EMPTY;
    }

}
