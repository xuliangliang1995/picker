package com.grasswort.picker.wechat.service;

import com.grasswort.picker.wechat.IWxMpUserInfoService;
import com.grasswort.picker.wechat.constants.SysRetCodeConstants;
import com.grasswort.picker.wechat.dto.WxMpUserInfoRequest;
import com.grasswort.picker.wechat.dto.WxMpUserInfoResponse;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author xuliangliang
 * @Classname WxMpUserInfoService
 * @Description 微信用户信息服务
 * @Date 2019/11/15 19:47
 * @blame Java Team
 */
@Service(version = "1.0", timeout = 10000)
public class WxMpUserInfoService implements IWxMpUserInfoService {

    @Autowired WxMpService wxMpService;

    /**
     * 获取微信用户信息
     *
     * @param wxMpUserInfoRequest
     * @return
     */
    @Override
    public WxMpUserInfoResponse wxMpUserInfo(WxMpUserInfoRequest wxMpUserInfoRequest) {
        WxMpUserInfoResponse response = new WxMpUserInfoResponse();

        String openId = wxMpUserInfoRequest.getOpenId();
        try {
            WxMpUser mpUser = wxMpService.getUserService().userInfo(openId);
            response.setHeadImgUrl(mpUser.getHeadImgUrl());
            response.setNickName(mpUser.getNickname());
            response.setSubscribe(mpUser.getSubscribe());
        } catch (WxErrorException e) {
            e.printStackTrace();
            response.setMsg(e.getMessage());
            response.setCode(SysRetCodeConstants.WX_ERROR.getCode());
            return response;
        }

        response.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
        response.setCode(SysRetCodeConstants.SUCCESS.getCode());
        return response;
    }
}
