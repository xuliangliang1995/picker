package com.grasswort.picker.wechat.service;

import com.grasswort.picker.wechat.IQrcodeService;
import com.grasswort.picker.wechat.constants.SysRetCodeConstants;
import com.grasswort.picker.wechat.dto.WxQrcodeRequest;
import com.grasswort.picker.wechat.dto.WxQrcodeResponse;
import com.grasswort.picker.wechat.util.QrcodeCiperEncrypt;
import com.grasswort.picker.wechat.util.QrcodeInfo;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author xuliangliang
 * @Classname QrcodeService
 * @Description 二维码服务
 * @Date 2019/11/14 19:23
 * @blame Java Team
 */
@Service(version = "1.0", timeout = 10000)
public class QrcodeService implements IQrcodeService {

    @Autowired WxMpService wxMpService;

    /**
     * 二维码生成
     *
     * @param wxQrcodeRequest
     * @return
     */
    @Override
    public WxQrcodeResponse generateQrcode(WxQrcodeRequest wxQrcodeRequest)  {
        WxQrcodeResponse response = new WxQrcodeResponse();

        String scene = wxQrcodeRequest.getScene();
        String text = wxQrcodeRequest.getText();
        String callback = wxQrcodeRequest.getCallback();
        Integer expireSeconds = wxQrcodeRequest.getExpireSeconds();

        QrcodeInfo qrcodeInfo = QrcodeInfo.Builder.aQrcodeInfo()
                .withScene(scene)
                .withText(text)
                .withCallback(callback)
                .build();
        String sceneText = QrcodeCiperEncrypt.INSTANCE.encrypt(qrcodeInfo);

        WxMpQrCodeTicket ticket = null;
        try {
            if (expireSeconds != null && expireSeconds > 0) {
                ticket = wxMpService.getQrcodeService().qrCodeCreateTmpTicket(sceneText, expireSeconds);
            } else {
                ticket = wxMpService.getQrcodeService().qrCodeCreateLastTicket(sceneText);
            }
        } catch (WxErrorException e) {
            e.printStackTrace();
            response.setCode(SysRetCodeConstants.WX_ERROR.getCode());
            response.setMsg(e.getMessage());
            return response;
        }
        response.setTicket(ticket.getTicket());
        response.setUrl(ticket.getUrl());
        response.setCode(SysRetCodeConstants.SUCCESS.getCode());
        response.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
        return response;
    }
}
