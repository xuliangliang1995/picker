package com.grasswort.picker.wechat;

import com.grasswort.picker.wechat.dto.WxMpUserInfoRequest;
import com.grasswort.picker.wechat.dto.WxMpUserInfoResponse;

/**
 * @author xuliangliang
 * @Classname IWxMpUserInfoService
 * @Description 微信用户信息服务
 * @Date 2019/11/15 19:46
 * @blame Java Team
 */
public interface IWxMpUserInfoService {

    /**
     * 获取微信用户信息
     * @param wxMpUserInfoRequest
     * @return
     */
    WxMpUserInfoResponse wxMpUserInfo(WxMpUserInfoRequest wxMpUserInfoRequest);
}
