package com.grasswort.picker.user.dto;

import com.grasswort.picker.commons.result.AbstractResponse;
import com.grasswort.picker.user.constants.SysRetCodeConstants;
import lombok.Data;

/**
 * @author xuliangliang
 * @Classname SendActivateEmailResponse
 * @Description 发送激活邮件响应
 * @Date 2019/10/8 16:13
 * @blame Java Team
 */
@Data
public class SendActivateEmailResponse extends AbstractResponse {

    private String email;

    @Override
    public boolean isSuccess() {
        return SysRetCodeConstants.SUCCESS.getCode().equals(this.getCode());
    }
}
