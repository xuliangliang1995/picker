package com.grasswort.picker.user.vo;


import com.grasswort.picker.user.validator.Password;
import com.grasswort.picker.user.validator.Username;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

/**
 * @author xuliangliang
 * @Classname SignUpForm
 * @Description 注册表单
 * @Date 2019/10/15 14:36
 * @blame Java Team
 */
@Data
public class SignUpForm {
    @NotNull
    @Username
    private String username;
    @NotNull
    @Password
    private String password;
    @NotNull
    @Email
    private String email;
}
