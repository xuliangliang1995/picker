package com.grasswort.picker.wechat.service.handler;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import me.chanjar.weixin.mp.builder.outxml.TextBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class SubscribeHandler extends AbstractHandler {
	@Autowired
	private ScanHandler scanHandler;

	@Override
	public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService,
                                    WxSessionManager sessionManager) throws WxErrorException {

	    this.logger.info("新关注用户 OPENID: " + wxMessage.getFromUser());

	    // 获取微信用户基本信息
	    WxMpUser userWxInfo = wxMpService.getUserService().userInfo(wxMessage.getFromUser(), null);

	    if (userWxInfo != null) {
	      // TODO 可以添加关注用户到本地
	    }

	    WxMpXmlOutMessage responseResult = null;
	    try {
	      responseResult = handleSpecial(wxMessage);
	    } catch (Exception e) {
	      this.logger.error(e.getMessage(), e);
	    }

	    if (responseResult != null) {
	      return responseResult;
	    }

	    try {
	      return new TextBuilder().content("感谢关注").build();
	    } catch (Exception e) {
	      this.logger.error(e.getMessage(), e);
	    }

	    return null;
	  }
	
   /**
	* 处理特殊请求，比如如果是扫码进来的，可以做相应处理
	*/
	protected WxMpXmlOutMessage handleSpecial(WxMpXmlMessage wxMessage) throws Exception {
		String eventKey = wxMessage.getEventKey();
		boolean isScanSubcribe = eventKey.contains("qrscene_");
		if (isScanSubcribe) {
			String qrcodeInfo = eventKey.replace("qrscene_", "");
			log.info("\n扫码关注：{}", qrcodeInfo);
			scanHandler.processQrcodeInfo(qrcodeInfo, wxMessage.getFromUser());
		}
	    return null;
	}

}
