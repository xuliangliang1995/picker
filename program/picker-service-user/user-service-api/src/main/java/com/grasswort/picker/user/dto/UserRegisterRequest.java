package com.grasswort.picker.user.dto;

import com.grasswort.picker.commons.result.AbstractRequest;
import lombok.Data;

/**
 * @author xuliangliang
 * @Classname UserRegisterRequest
 * @Description TODO
 * @Date 2019/9/21 17:07
 * @blame Java Team
 */
@Data
public class UserRegisterRequest extends AbstractRequest {

    private String username;

    private String password;

    private String email;

    @Override
    public void requestCheck() {}
}
