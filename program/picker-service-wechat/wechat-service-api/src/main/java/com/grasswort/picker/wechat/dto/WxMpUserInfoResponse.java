package com.grasswort.picker.wechat.dto;

import com.grasswort.picker.commons.result.AbstractResponse;
import com.grasswort.picker.wechat.constants.SysRetCodeConstants;
import lombok.Data;

/**
 * @author xuliangliang
 * @Classname WxMpUserInfoResponse
 * @Description 用户信息
 * @Date 2019/11/15 19:54
 * @blame Java Team
 */
@Data
public class WxMpUserInfoResponse extends AbstractResponse {
    /**
     * 头像
     */
    private String headImgUrl;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 是否关注（卷耳 Picker）
     */
    private Boolean subscribe;

    @Override
    public boolean isSuccess() {
        return SysRetCodeConstants.SUCCESS.getCode().equals(this.getCode());
    }

}
