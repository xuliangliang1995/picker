package com.grasswort.picker.wechat.service;

import com.grasswort.picker.commons.constants.cluster.ClusterFaultMechanism;
import com.grasswort.picker.wechat.IWechatMpService;
import com.grasswort.picker.wechat.constants.SysRetCodeConstants;
import com.grasswort.picker.wechat.dto.WxMpAuthRequest;
import com.grasswort.picker.wechat.dto.WxMpAuthResponse;
import com.grasswort.picker.wechat.dto.WxMpCallbackRequest;
import com.grasswort.picker.wechat.dto.WxMpCallbackResponse;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.apache.commons.lang3.StringUtils;
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

    @Autowired WxMpMessageRouter wxMpMessageRouter;

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
            log.info("\n认证结果：{}", response);
            return response;
        }

        response.setResult("非法请求");
        response.setCode(SysRetCodeConstants.INVALID_REQUEST.getCode());
        response.setMsg(SysRetCodeConstants.INVALID_REQUEST.getMsg());
        log.info("\n认证结果：{}", response);
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
        WxMpCallbackResponse response = new WxMpCallbackResponse();

        String signature = callbackRequest.getSignature();
        String encType = callbackRequest.getEncType();
        String msgSignature = callbackRequest.getMsgSignature();
        String timestamp = callbackRequest.getTimestamp();
        String nonce = callbackRequest.getNonce();
        String requestBody = callbackRequest.getRequestBody();

        log.info("\n接收微信请求：[signature=[{}], encType=[{}], msgSignature=[{}],"
                        + " timestamp=[{}], nonce=[{}], requestBody=[\n{}\n] ",
                signature, encType, msgSignature, timestamp, nonce, requestBody);

        if (! wxMpService.checkSignature(timestamp, nonce, signature)) {
            response.setResult("非法请求，可能属于伪造的请求！");
            response.setMsg(SysRetCodeConstants.INVALID_REQUEST.getMsg());
            response.setCode(SysRetCodeConstants.INVALID_REQUEST.getCode());
            return response;
        }

        String out = null;
        if (encType == null) {
            // 明文传输的消息
            WxMpXmlMessage inMessage = WxMpXmlMessage.fromXml(requestBody);
            WxMpXmlOutMessage outMessage = wxMpMessageRouter.route(inMessage);
            if (outMessage != null) {
                out = outMessage.toXml();
            }
        } else if ("aes".equals(encType)) {
            // aes加密的消息
            WxMpXmlMessage inMessage = WxMpXmlMessage.fromEncryptedXml(requestBody,
                    wxMpService.getWxMpConfigStorage(), timestamp, nonce, msgSignature);
            log.debug("\n消息解密后内容为：\n{} ", inMessage.toString());
            WxMpXmlOutMessage outMessage = wxMpMessageRouter.route(inMessage);
            if (outMessage != null) {
                out = outMessage.toEncryptedXml(wxMpService.getWxMpConfigStorage());
            }
        }
        log.debug("\n组装回复信息：{}", out);

        response.setResult(StringUtils.isNotBlank(out) ? out : StringUtils.EMPTY);
        response.setMsg(SysRetCodeConstants.INVALID_REQUEST.getMsg());
        response.setCode(SysRetCodeConstants.INVALID_REQUEST.getCode());
        return response;
    }
}
