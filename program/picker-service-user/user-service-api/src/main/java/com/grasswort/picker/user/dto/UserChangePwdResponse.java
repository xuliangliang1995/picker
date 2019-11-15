package com.grasswort.picker.user.dto;

import com.grasswort.picker.commons.result.AbstractResponse;
import com.grasswort.picker.user.constants.SysRetCodeConstants;
import lombok.Data;

/**
 * @author xuliangliang
 * @Classname UserChangePwdResponse
 * @Description 修改密码响应
 * @Date 2019/10/12 16:36
 * @blame Java Team
 */
@Data
public class UserChangePwdResponse extends AbstractResponse {
    /**
     * new access_token
     */
    private String accessToken;

    /**
     * new refresh_token
     */
    private String refreshToken;

    @Override
    public boolean isSuccess() {
        return SysRetCodeConstants.SUCCESS.getCode().equals(this.getCode());
    }
}
