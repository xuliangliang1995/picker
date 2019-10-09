package com.grasswort.picker.user.vo;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author xuliangliang
 * @Classname UserActivateForm
 * @Description 用户激活表单
 * @Date 2019/10/7 13:05
 * @blame Java Team
 */
@Data
public class UserActivateForm {
    @NotNull
    @Length(min = 8, max = 20)
    private String username;
    @NotNull
    @Length(min = 32, max = 32)
    private String code;
    @NotNull
    @Min(1)
    private Long activateId;
}
