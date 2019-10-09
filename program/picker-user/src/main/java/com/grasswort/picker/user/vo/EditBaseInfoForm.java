package com.grasswort.picker.user.vo;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author xuliangliang
 * @Classname EditBaseInfoForm
 * @Description 编辑用户信息表单
 * @Date 2019/10/9 13:34
 * @blame Java Team
 */
@Data
public class EditBaseInfoForm {
    @NotNull
    @Length(min = 2, max = 10)
    private String name;
    @NotNull
    @Min(0)
    @Max(2)
    private Byte sex;
    @Length(min = 11, max = 11)
    private String phone;
    @NotNull
    @Email
    private String email;
}
