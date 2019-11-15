package com.grasswort.picker.wechat.service.handler;

import com.alibaba.fastjson.JSONObject;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutTextMessage;
import me.chanjar.weixin.mp.builder.outxml.TextBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class MsgHandler extends AbstractHandler {

	@Override
	public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService,
                                    WxSessionManager sessionManager) throws WxErrorException {

	    if (! wxMessage.getMsgType().equals(WxConsts.XmlMsgType.EVENT)) {
	      // TODO 可以选择将消息保存到本地
	    }

	    // 当用户输入关键词如“你好”，“客服”等，并且有客服在线时，把消息转发给在线客服
	    if (StringUtils.startsWithAny(wxMessage.getContent(), "你好", "客服")
	      && wxMpService.getKefuService().kfOnlineList()
	      .getKfOnlineList().size()>0) {
	      return WxMpXmlOutMessage
	        .TRANSFER_CUSTOMER_SERVICE().fromUser(wxMessage.getToUser())
	        .toUser(wxMessage.getFromUser()).build();
	    }

	    if (wxMessage.getMsgType().equals(WxConsts.MediaFileType.FILE)) {
			System.out.println(JSONObject.toJSONString(wxMessage));
		}

	    // 判断是否是命令消息
	    String text = "倘若终究要痴，为花而痴，不也很美么。";
		WxMpXmlOutTextMessage outTextMessage = WxMpXmlOutTextMessage.TEXT().content(text).build();
		outTextMessage.setCreateTime(System.currentTimeMillis());
		outTextMessage.setToUserName(wxMessage.getFromUser());
	    return outTextMessage;

	  }

}
