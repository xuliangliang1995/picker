package com.grasswort.picker.wechat.service.handler;

import com.alibaba.fastjson.JSON;
import com.grasswort.picker.wechat.util.QrcodeCiperEncrypt;
import com.grasswort.picker.wechat.util.QrcodeInfo;
import com.grasswort.picker.wechat.vo.CallBackForm;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
public class ScanHandler extends AbstractHandler {
    @Autowired
    private OkHttpClient okHttpClient;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMpXmlMessage, Map<String, Object> map, WxMpService wxMpService, WxSessionManager wxSessionManager) throws WxErrorException {
        String eventKey = wxMpXmlMessage.getEventKey();
        log.info("\n扫码事件：{}", eventKey);
        this.processQrcodeInfo(eventKey, wxMpXmlMessage.getFromUser());
        return null;
    }

    /**
     * 处理扫码事件
     * @param eventKey
     * @param openId
     */
    public void processQrcodeInfo(String eventKey, String openId) {
        QrcodeInfo qrcodeInfo = QrcodeCiperEncrypt.INSTANCE.decrypt(eventKey);
        if (qrcodeInfo != null) {
            CallBackForm form = new CallBackForm();
            form.setBody(eventKey);
            form.setOpenId(openId);
            RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8")
                    , JSON.toJSONString(form));
            Request request = new Request.Builder()
                    .url("https://picker.grasswort.com".concat(qrcodeInfo.getCallback()))
                    .post(requestBody)
                    .build();
            try {
                okHttpClient.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}