package com.grasswort.picker.user.dto;

import com.grasswort.picker.commons.result.AbstractRequest;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author xuliangliang
 * @Classname UserLoginRequest
 * @Description 登录
 * @Date 2019/9/21 16:59
 * @blame Java Team
 */
@Data
public class UserLoginRequest extends AbstractRequest {
    @NotNull
    @Size(min = 8, max = 20)
    private String username;
    @NotNull
    @Size(min = 8, max = 20)
    private String password;

    @Override
    public void requestCheck() {

    }
}
