package com.grasswort.picker.user.dto;

import com.grasswort.picker.commons.result.AbstractRequest;
import lombok.Data;

/**
 * @author xuliangliang
 * @Classname UserLoginRequest
 * @Description TODO
 * @Date 2019/9/21 16:59
 * @blame Java Team
 */
@Data
public class UserLoginRequest extends AbstractRequest {

    private String username;

    private String password;

    @Override
    public void requestCheck() {

    }
}
