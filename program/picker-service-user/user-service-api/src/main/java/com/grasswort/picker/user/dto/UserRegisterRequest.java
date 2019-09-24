package com.grasswort.picker.user.dto;

import com.grasswort.picker.commons.result.AbstractRequest;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author xuliangliang
 * @Classname UserRegisterRequest
 * @Description TODO
 * @Date 2019/9/21 17:07
 * @blame Java Team
 */
@Data
public class UserRegisterRequest extends AbstractRequest {
    @NotNull
    @Size(min = 8, max = 20)
    private String username;
    @NotNull
    @Size(min = 8, max = 20)
    private String password;
    @NotNull
    @Email
    private String email;

    @Override
    public void requestCheck() {
        System.out.println("执行参数校验！");
    }
}
