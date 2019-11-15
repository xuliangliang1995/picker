package com.grasswort.picker.user.dto;

import com.grasswort.picker.commons.result.AbstractResponse;
import com.grasswort.picker.user.constants.SysRetCodeConstants;
import lombok.Data;

/**
 * @author xuliangliang
 * @Classname ChangeMpOpenIdResponse
 * @Description 更换 openId 请求
 * @Date 2019/11/15 14:10
 * @blame Java Team
 */
@Data
public class ChangeMpOpenIdResponse extends AbstractResponse {
    @Override
    public boolean isSuccess() {
        return SysRetCodeConstants.SUCCESS.getCode().equals(this.getCode());
    }
}
