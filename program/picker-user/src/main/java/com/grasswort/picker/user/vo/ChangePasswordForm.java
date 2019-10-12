package com.grasswort.picker.user.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
    @Size(min = 8, max = 20)
    private String password;

    @Override
    public String toString() {
        return "ChangePasswordForm{" +
                "password=******'"  + '\'' +
                '}';
    }
}
