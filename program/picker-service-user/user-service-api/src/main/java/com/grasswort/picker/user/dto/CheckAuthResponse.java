package com.grasswort.picker.user.dto;

import com.grasswort.picker.commons.result.AbstractResponse;
import com.grasswort.picker.user.constants.SysRetCodeConstants;
import lombok.Data;

/**
 * @author xuliangliang
 * @Classname CheckAuthResponse
 * @Description 基本信息
 * @Date 2019/9/21 17:05
 * @blame Java Team
 */
@Data
public class CheckAuthResponse extends AbstractResponse {

    private Long id;

    private String name;

    private boolean privilege;

    @Override
    public boolean isSuccess() {
        return SysRetCodeConstants.SUCCESS.getCode().equals(this.getCode());
    }
}
