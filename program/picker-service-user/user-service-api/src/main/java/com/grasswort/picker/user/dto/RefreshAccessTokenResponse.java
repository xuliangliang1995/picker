package com.grasswort.picker.user.dto;

import com.grasswort.picker.commons.result.AbstractResponse;
import com.grasswort.picker.user.constants.SysRetCodeConstants;
import lombok.Data;

/**
 * @author xuliangliang
 * @Classname RefreshAccessTokenResponse
 * @Description 刷新 token 结果
 * @Date 2019/10/8 21:33
 * @blame Java Team
 */
@Data
public class RefreshAccessTokenResponse extends AbstractResponse  {
    /**
     * access_token
     */
    private String accessToken;
    /**
     * refresh_token
     */
    private String refreshToken;

    @Override
    public boolean isSuccess() {
        return SysRetCodeConstants.SUCCESS.getCode().equals(this.getCode());
    }
}
