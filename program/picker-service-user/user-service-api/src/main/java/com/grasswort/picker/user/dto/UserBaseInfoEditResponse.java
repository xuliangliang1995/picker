package com.grasswort.picker.user.dto;

import com.grasswort.picker.commons.result.AbstractResponse;
import com.grasswort.picker.user.constants.SysRetCodeConstants;
import lombok.Data;

/**
 * @author xuliangliang
 * @Classname UserBaseInfoEditResponse
 * @Description 编辑基本信息
 * @Date 2019/10/9 12:42
 * @blame Java Team
 */
@Data
public class UserBaseInfoEditResponse extends AbstractResponse {

    private String name;

    private String phone;

    private String email;

    private Byte sex;

    private String avatar;

    @Override
    public boolean isSuccess() {
        return SysRetCodeConstants.SUCCESS.getCode().equals(this.getCode());
    }
}
