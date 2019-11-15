package com.grasswort.picker.user.dto;

import com.grasswort.picker.commons.result.AbstractResponse;
import com.grasswort.picker.user.constants.SysRetCodeConstants;

/**
 * @author xuliangliang
 * @Classname ChangePhoneResponse
 * @Description 修改手机号结果
 * @Date 2019/11/3 19:59
 * @blame Java Team
 */
public class UserChangePhoneResponse extends AbstractResponse {

    @Override
    public boolean isSuccess() {
        return SysRetCodeConstants.SUCCESS.getCode().equals(this.getCode());
    }
}
