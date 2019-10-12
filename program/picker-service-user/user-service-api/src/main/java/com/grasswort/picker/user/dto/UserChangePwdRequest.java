package com.grasswort.picker.user.dto;

import com.grasswort.picker.commons.result.AbstractRequest;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author xuliangliang
 * @Classname UserChangePwdRequest
 * @Description 修改密码
 * @Date 2019/10/12 16:36
 * @blame Java Team
 */
@Data
public class UserChangePwdRequest extends AbstractRequest {
    @NotNull
    @Size(min = 8, max = 20)
    private String password;

    @NotEmpty
    private String ip;

    @NotEmpty
    private String accessToken;

    @Override
    public void requestCheck() {

    }
}
