package com.grasswort.picker.user.dto;

import com.grasswort.picker.commons.result.AbstractResponse;
import com.grasswort.picker.user.constants.SysRetCodeConstants;

/**
 * @author xuliangliang
 * @Classname SaveSettingResponse
 * @Description 保存设置
 * @Date 2019/11/4 15:50
 * @blame Java Team
 */
public class SaveSettingResponse extends AbstractResponse {

    @Override
    public boolean isSuccess() {
        return SysRetCodeConstants.SUCCESS.getCode().equals(this.getCode());
    }
}
