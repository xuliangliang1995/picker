package com.grasswort.picker.user.vo;

import com.grasswort.picker.user.validator.Password;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author xuliangliang
 * @Classname ChangePasswordForm
 * @Description 修改密码表单
 * @Date 2019/10/12 17:16
 * @blame Java Team
 */
@Data
public class ChangePasswordForm {
    @NotNull
    @Password
    private String password;

    @Override
    public String toString() {
        return "ChangePasswordForm{" +
                "password=******'"  + '\'' +
                '}';
    }
}
