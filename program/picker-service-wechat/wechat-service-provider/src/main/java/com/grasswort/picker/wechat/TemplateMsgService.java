package com.grasswort.picker.wechat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.grasswort.picker.wechat.constants.SysRetCodeConstants;
import com.grasswort.picker.wechat.dto.WxMpTemplateMsgRequest;
import com.grasswort.picker.wechat.dto.WxMpTemplateMsgResponse;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xuliangliang
 * @Classname TemplateMsgService
 * @Description 模板消息服务
 * @Date 2019/11/15 15:05
 * @blame Java Team
 */
@Service(version = "1.0", timeout = 10000)
public class TemplateMsgService implements ITemplateMsgService {

    @Autowired WxMpService wxMpService;

    /**
     * 发送模板消息
     *
     * @param wxMpTemplateMsgRequest
     * @return
     */
    @Override
    public WxMpTemplateMsgResponse sendTemplateMsg(WxMpTemplateMsgRequest wxMpTemplateMsgRequest) {
        WxMpTemplateMsgResponse response = new WxMpTemplateMsgResponse();

        String templateId = wxMpTemplateMsgRequest.getTemplateId();
        String toUserId = wxMpTemplateMsgRequest.getToOpenId();
        String jsonData = wxMpTemplateMsgRequest.getJson();
        String url = wxMpTemplateMsgRequest.getUrl();
        String miniProgramAppId = wxMpTemplateMsgRequest.getMiniProgramAppid();
        String miniProgramPagePath = wxMpTemplateMsgRequest.getMiniProgramPagePath();

        List<WxMpTemplateData> dataList = new ArrayList<>();
        JSONObject dataObj = JSON.parseObject(jsonData);
        dataObj.keySet().forEach(key -> dataList.add(new WxMpTemplateData(key, String.valueOf(dataObj.get(key)))));

        WxMpTemplateMessage.WxMpTemplateMessageBuilder builder = WxMpTemplateMessage.builder();
        builder.toUser(toUserId).templateId(templateId).data(dataList);

        if (StringUtils.isNotBlank(url)) {
            builder.url(url);
        }
        if (StringUtils.isNotBlank(miniProgramAppId)) {
            builder.miniProgram(new WxMpTemplateMessage.MiniProgram(miniProgramAppId, miniProgramPagePath, StringUtils.isNotBlank(miniProgramPagePath)));
        }

        try {
            wxMpService.getTemplateMsgService().sendTemplateMsg(builder.build());
        } catch (WxErrorException e) {
            e.printStackTrace();
            response.setCode(SysRetCodeConstants.WX_ERROR.getCode());
            response.setMsg(e.getMessage());
            return response;
        }

        response.setCode(SysRetCodeConstants.SUCCESS.getCode());
        response.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
        return response;
    }
}
