package com.grasswort.picker.user.dto;

import com.grasswort.picker.commons.result.AbstractRequest;
import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

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
    @Length(min = 8, max = 20)
    private String username;
    @NotNull
    @Length(min = 8, max = 20)
    private String password;
    @NotNull
    @Email
    private String email;

    @Override
    public void requestCheck() {
        System.out.println("执行参数校验！");
    }
}
