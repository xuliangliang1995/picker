package com.grasswort.picker.user.vo;

import com.grasswort.picker.user.validator.Mobile;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
    @Size(min = 2, max = 10)
    private String name;
    @NotNull
    @Min(0)
    @Max(2)
    private Byte sex;
    @Mobile
    private String phone;
    @Size(max = 120)
    private String avatar;
}
