package com.grasswort.picker.user.vo;

import lombok.Data;

/**
 * @author xuliangliang
 * @Classname LoginForm
 * @Description 登录表单数据
 * @Date 2019/10/2 15:12
 * @blame Java Team
 */
@Data
public class LoginForm {

    private String username;

    private String password;

    private String verifyCode;

    @Override
    public String toString() {
        return "LoginForm{" +
                "username='" + username + '\'' +
                ", password='******" + '\'' +
                ", verifyCode='" + verifyCode + '\'' +
                '}';
    }

}
