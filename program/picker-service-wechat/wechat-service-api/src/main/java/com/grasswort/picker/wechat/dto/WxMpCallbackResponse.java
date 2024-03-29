package com.grasswort.picker.wechat.dto;

import com.grasswort.picker.commons.result.AbstractResponse;
import com.grasswort.picker.wechat.constants.SysRetCodeConstants;
import lombok.Data;

/**
 * @author xuliangliang
 * @Classname WxMpCallbackResponse
 * @Description 微信回调响应
 * @Date 2019/11/14 15:35
 * @blame Java Team
 */
@Data
public class WxMpCallbackResponse extends AbstractResponse {

    private String result;

    @Override
    public boolean isSuccess() {
        return SysRetCodeConstants.SUCCESS.getCode().equals(this.getCode());
    }
}
