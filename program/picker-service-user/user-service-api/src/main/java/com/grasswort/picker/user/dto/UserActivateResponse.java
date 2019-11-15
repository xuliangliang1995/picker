package com.grasswort.picker.user.dto;

import com.grasswort.picker.commons.result.AbstractResponse;
import com.grasswort.picker.user.constants.SysRetCodeConstants;

/**
 * @author xuliangliang
 * @Classname UserActivateResponse
 * @Description 用户激活响应
 * @Date 2019/10/6 22:33
 * @blame Java Team
 */
public class UserActivateResponse extends AbstractResponse {

    @Override
    public boolean isSuccess() {
        return SysRetCodeConstants.SUCCESS.getCode().equals(this.getCode());
    }
}
