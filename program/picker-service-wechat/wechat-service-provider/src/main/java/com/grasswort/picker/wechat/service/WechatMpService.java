package com.grasswort.picker.wechat.service;

import com.grasswort.picker.commons.constants.cluster.ClusterFaultMechanism;
import com.grasswort.picker.wechat.IWechatMpService;
import com.grasswort.picker.wechat.constants.SysRetCodeConstants;
import com.grasswort.picker.wechat.dto.WxMpAuthRequest;
import com.grasswort.picker.wechat.dto.WxMpAuthResponse;
import com.grasswort.picker.wechat.dto.WxMpCallbackRequest;
import com.grasswort.picker.wechat.dto.WxMpCallbackResponse;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author xuliangliang
 * @Classname WechatMpService
 * @Description 微信公众号服务
 * @Date 2019/11/14 15:16
 * @blame Java Team
 */
@Slf4j
@Service(version = "1.0", timeout = 10000, cluster = ClusterFaultMechanism.FAIL_OVER)
public class WechatMpService implements IWechatMpService {

    @Autowired WxMpService wxMpService;

    /**
     * 回调地址认证
     *
     * @param authRequest
     * @return
     */
    @Override
    public WxMpAuthResponse authGet(WxMpAuthRequest authRequest) {
        WxMpAuthResponse response = new WxMpAuthResponse();

        String signature = authRequest.getSignature();
        String timestamp = authRequest.getTimestamp();
        String nonce = authRequest.getNonce();
        String echostr = authRequest.getEchostr();
        log.info("\n接收到来自微信服务器的认证消息：[{}, {}, {}, {}]", signature, timestamp, nonce, echostr);

        if (wxMpService.checkSignature(timestamp, nonce, signature)) {
            response.setResult(echostr);
            response.setCode(SysRetCodeConstants.SUCCESS.getCode());
            response.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
            return response;
        }

        response.setResult("非法请求");
        response.setCode(SysRetCodeConstants.INVALID_REQUEST.getCode());
        response.setMsg(SysRetCodeConstants.INVALID_REQUEST.getMsg());
        return response;
    }

    /**
     * 处理微信回调请求
     *
     * @param callbackRequest
     * @return
     */
    @Override
    public WxMpCallbackResponse processCallback(WxMpCallbackRequest callbackRequest) {
        return null;
    }
}
