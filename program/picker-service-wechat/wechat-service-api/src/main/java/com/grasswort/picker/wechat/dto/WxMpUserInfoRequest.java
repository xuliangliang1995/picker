package com.grasswort.picker.wechat.dto;

import com.grasswort.picker.commons.result.AbstractRequest;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author xuliangliang
 * @Classname WxMpUserInfoRequest
 * @Description 用户信息请求
 * @Date 2019/11/15 19:53
 * @blame Java Team
 */
@Data
public class WxMpUserInfoRequest extends AbstractRequest {
    @NotNull
    @Size(min = 28, max = 28)
    private String openId;

    @Override
    public void requestCheck() {

    }
}
