package com.grasswort.picker.user.vo;

import com.grasswort.picker.user.validator.Password;
import com.grasswort.picker.user.validator.Username;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author xuliangliang
 * @Classname LoginForm
 * @Description 登录表单数据
 * @Date 2019/10/2 15:12
 * @blame Java Team
 */
@Data
public class LoginForm {
    @NotNull
    @Username
    private String username;
    @NotNull
    @Password
    private String password;

    @Override
    public String toString() {
        return "LoginForm{" +
                "username='" + username + '\'' +
                ", password='******" + '\'' +
                '}';
    }
}
