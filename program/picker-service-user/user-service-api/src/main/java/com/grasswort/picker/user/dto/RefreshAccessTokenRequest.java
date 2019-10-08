package com.grasswort.picker.user.dto;

import com.grasswort.picker.commons.result.AbstractRequest;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author xuliangliang
 * @Classname RefreshAccessTokenRequest
 * @Description
 * @Date 2019/10/8 21:34
 * @blame Java Team
 */
@Data
public class RefreshAccessTokenRequest extends AbstractRequest {
    @NotEmpty
    private String refreshToken;

    @Override
    public void requestCheck() {

    }
}
