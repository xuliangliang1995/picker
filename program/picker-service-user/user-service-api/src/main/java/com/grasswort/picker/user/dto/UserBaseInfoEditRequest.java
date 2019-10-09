package com.grasswort.picker.user.dto;

import com.grasswort.picker.commons.result.AbstractRequest;
import lombok.Data;
import javax.validation.constraints.Size;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

/**
 * @author xuliangliang
 * @Classname UserBaseInfoEditRequest
 * @Description 编辑用户基本信息
 * @Date 2019/10/9 12:40
 * @blame Java Team
 */
@Data
public class UserBaseInfoEditRequest extends AbstractRequest {
    private Long userId;
    @NotNull
    @Size(min = 2, max = 10)
    private String name;
    @NotNull
    @Size(min = 11, max = 11)
    private String phone;
    @Email
    private String email;
    @NotNull
    private Byte sex;

    @Override
    public void requestCheck() {

    }
}
