package com.grasswort.picker.user.vo;

import lombok.Data;
import javax.validation.constraints.Size;

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
    @Size(min = 8, max = 20)
    private String username;
    @NotNull
    @Size(min = 8, max = 20)
    private String password;

    @Override
    public String toString() {
        return "LoginForm{" +
                "username='" + username + '\'' +
                ", password='******" + '\'' +
                '}';
    }
}
