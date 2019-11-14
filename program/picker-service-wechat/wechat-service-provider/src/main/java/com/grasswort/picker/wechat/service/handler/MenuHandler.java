package com.grasswort.picker.wechat.service.handler;

import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class MenuHandler extends AbstractHandler {

	@Override
	public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService,
                                    WxSessionManager sessionManager) throws WxErrorException {
	  String msg = String.format("type:%s, event:%s, key:%s",
	    	      wxMessage.getMsgType(), wxMessage.getEvent(),
	    	      wxMessage.getEventKey());
	  if (WxConsts.MenuButtonType.VIEW.equals(wxMessage.getEvent())) {
	      return null;
	  }
	  String eventKey = wxMessage.getEventKey();

	  return WxMpXmlOutMessage.TEXT().content(msg)
	      .fromUser(wxMessage.getToUser()).toUser(wxMessage.getFromUser())
	      .build();
	  }

}
