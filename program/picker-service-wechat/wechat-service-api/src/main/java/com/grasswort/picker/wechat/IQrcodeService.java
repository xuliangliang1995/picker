package com.grasswort.picker.wechat;

import com.grasswort.picker.wechat.dto.WxQrcodeRequest;
import com.grasswort.picker.wechat.dto.WxQrcodeResponse;

/**
 * @author xuliangliang
 * @Classname IQrcodeService
 * @Description 二维码生成服务
 * @Date 2019/11/14 19:15
 * @blame Java Team
 */
public interface IQrcodeService {
    /**
     * 二维码生成
     * @param wxQrcodeRequest
     * @return
     */
    WxQrcodeResponse generateQrcode(WxQrcodeRequest wxQrcodeRequest);
}
