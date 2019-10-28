package com.grasswort.picker.user.vo;

import com.grasswort.picker.user.validator.Username;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author xuliangliang
 * @Classname QueryActivateStatusForm
 * @Description 查看账号激活状态表单
 * @Date 2019/10/15 16:33
 * @blame Java Team
 */
@Data
public class QueryActivateStatusForm {
    @NotNull
    @Username
    private String username;
}
