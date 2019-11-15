package com.grasswort.picker.user.dto;

import com.grasswort.picker.commons.result.AbstractResponse;
import com.grasswort.picker.user.constants.SysRetCodeConstants;
import lombok.Data;

/**
 * @author xuliangliang
 * @Classname UserLoginResponse
 * @Description TODO
 * @Date 2019/9/21 16:57
 * @blame Java Team
 */
@Data
public class UserLoginResponse extends AbstractResponse {
    private String username;
    private String phone;
    private String email;
    private Integer sex;
    private String accessToken;
    private String refreshToken;

    @Override
    public boolean isSuccess() {
        return SysRetCodeConstants.SUCCESS.getCode().equals(this.getCode());
    }
}
