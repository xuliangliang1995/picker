package com.grasswort.picker.wechat.controller;

import com.grasswort.picker.wechat.IWechatMpService;
import com.grasswort.picker.wechat.constants.SysRetCodeConstants;
import com.grasswort.picker.wechat.dto.WxMpAuthRequest;
import com.grasswort.picker.wechat.dto.WxMpAuthResponse;
import com.grasswort.picker.wechat.dto.WxMpCallbackRequest;
import com.grasswort.picker.wechat.dto.WxMpCallbackResponse;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@RestController
@RequestMapping("/")
public class WxMpController {

    @Reference(version = "1.0", timeout = 10000)
    IWechatMpService iWechatMpService;

    // https://picker.grasswort.com/api/wechat?signature=43712f5b09acfa22af75ee6604ba1db986beb2c7&timestamp=1573728142&nonce=1350192340&echostr=1184270597062864894
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
        if (SysRetCodeConstants.SUCCESS.getCode().equals(authResponse.getCode())) {
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
        WxMpCallbackRequest callbackRequest = WxMpCallbackRequest.Builder.aWxMpCallbackRequest()
                .withRequestBody(requestBody)
                .withSignature(signature)
                .withEncType(encType)
                .withMsgSignature(msgSignature)
                .withTimestamp(timestamp)
                .withNonce(nonce)
                .build();
        log.info(callbackRequest.toString());
        WxMpCallbackResponse callbackResponse = iWechatMpService.processCallback(callbackRequest);
        log.info(callbackResponse.toString());
        if (SysRetCodeConstants.SUCCESS.getCode().equals(callbackResponse.getCode())) {
            return callbackResponse.getResult();
        }
        return StringUtils.EMPTY;
    }

}
