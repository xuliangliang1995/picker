package com.grasswort.picker.user.vo;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author xuliangliang
 * @Classname UserSettingForm
 * @Description 用户配置
 * @Date 2019/11/4 16:16
 * @blame Java Team
 */
@Data
public class UserSettingForm {
    @NotEmpty
    @Size(min = 1, max = 20)
    private String markdownTheme;
    @NotNull
    @Min(0)
    private Integer safetyCheckMode;
    @NotNull
    private Boolean openBlogPush;
    @NotNull
    @Min(0)
    private Integer blogPushMode;

    private String blogPushTime;
}
