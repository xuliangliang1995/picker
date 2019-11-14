package com.grasswort.picker.wechat;

import com.grasswort.picker.wechat.dto.WxMpAuthRequest;
import com.grasswort.picker.wechat.dto.WxMpAuthResponse;
import com.grasswort.picker.wechat.dto.WxMpCallbackRequest;
import com.grasswort.picker.wechat.dto.WxMpCallbackResponse;
import org.springframework.validation.annotation.Validated;

/**
 * @author xuliangliang
 * @Classname IWechatMpService
 * @Description 微信公众号接口
 * @Date 2019/11/14 14:58
 * @blame Java Team
 */
public interface IWechatMpService {
    /**
     * 回调地址认证
     * @param authRequest
     * @return
     */
    WxMpAuthResponse authGet(@Validated WxMpAuthRequest authRequest);

    /**
     * 处理微信回调请求
     * @param callbackRequest
     * @return
     */
    WxMpCallbackResponse processCallback(@Validated WxMpCallbackRequest callbackRequest);
}
