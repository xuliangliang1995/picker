package com.grasswort.picker.user.vo;

import com.grasswort.picker.user.validator.Username;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author xuliangliang
 * @Classname ActivateForm
 * @Description 发送激活邮件表单
 * @Date 2019/10/15 16:18
 * @blame Java Team
 */
@Data
public class SendActivateEmailForm {
    @NotNull
    @Username
    private String username;

}
