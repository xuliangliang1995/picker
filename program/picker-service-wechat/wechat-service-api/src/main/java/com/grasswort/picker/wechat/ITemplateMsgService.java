package com.grasswort.picker.wechat;

import com.alibaba.fastjson.JSONObject;
import com.grasswort.picker.wechat.dto.WxMpTemplateMsgRequest;
import com.grasswort.picker.wechat.dto.WxMpTemplateMsgResponse;

/**
 * @author xuliangliang
 * @Classname ITemplateService
 * @Description 模板消息
 * @Date 2019/11/15 15:01
 * @blame Java Team
 */
public interface ITemplateMsgService {
    /**
     * 发送模板消息
     * @param wxMpTemplateMsgRequest
     * @return
     */
    WxMpTemplateMsgResponse sendTemplateMsg(WxMpTemplateMsgRequest wxMpTemplateMsgRequest);

}
