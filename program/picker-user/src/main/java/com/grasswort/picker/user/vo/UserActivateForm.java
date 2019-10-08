package com.grasswort.picker.user.vo;

import lombok.Data;

/**
 * @author xuliangliang
 * @Classname UserActivateForm
 * @Description 用户激活表单
 * @Date 2019/10/7 13:05
 * @blame Java Team
 */
@Data
public class UserActivateForm {

    private String username;

    private String code;

    private Long activateId;
}
