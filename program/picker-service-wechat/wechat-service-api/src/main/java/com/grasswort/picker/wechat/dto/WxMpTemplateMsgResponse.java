package com.grasswort.picker.wechat.dto;

import com.grasswort.picker.commons.result.AbstractResponse;
import com.grasswort.picker.wechat.constants.SysRetCodeConstants;
import lombok.Data;

/**
 * @author xuliangliang
 * @Classname WxMpTemplateMsgResponse
 * @Description 模板消息响应
 * @Date 2019/11/15 15:03
 * @blame Java Team
 */
@Data
public class WxMpTemplateMsgResponse extends AbstractResponse {

    @Override
    public boolean isSuccess() {
        return SysRetCodeConstants.SUCCESS.getCode().equals(this.getCode());
    }
}
