package com.grasswort.picker.user.vo;

import lombok.Data;
import javax.validation.constraints.Size;

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
    @Size(min = 8, max = 20)
    private String username;
    @NotNull
    @Size(min = 32, max = 32)
    private String code;
    @NotNull
    @Min(1)
    private Long activateId;
}
