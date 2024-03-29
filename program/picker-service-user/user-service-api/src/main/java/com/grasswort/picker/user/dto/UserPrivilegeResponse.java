package com.grasswort.picker.user.dto;

import com.grasswort.picker.commons.result.AbstractResponse;
import com.grasswort.picker.user.constants.SysRetCodeConstants;
import lombok.Data;

/**
 * @author xuliangliang
 * @Classname UserPrivilegeResponse
 * @Description 提高权限结果
 * @Date 2019/10/10 20:05
 * @blame Java Team
 */
@Data
public class UserPrivilegeResponse extends AbstractResponse {
    /**
     * 新的 accessToken
     */
    private String accessToken;

    @Override
    public boolean isSuccess() {
        return SysRetCodeConstants.SUCCESS.getCode().equals(this.getCode());
    }
}
