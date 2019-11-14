package com.grasswort.picker.wechat.dto;

import com.grasswort.picker.commons.result.AbstractResponse;
import lombok.Data;

/**
 * @author xuliangliang
 * @Classname WxMpAuthResponse
 * @Description 微信回调地址认证响应
 * @Date 2019/11/14 15:34
 * @blame Java Team
 */
@Data
public class WxMpAuthResponse extends AbstractResponse {

    private String result;
}
