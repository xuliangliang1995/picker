package com.grasswort.picker.user.dto;

import com.grasswort.picker.commons.result.AbstractResponse;
import lombok.Data;

/**
 * @author xuliangliang
 * @Classname UserRegisterResponse
 * @Description TODO
 * @Date 2019/9/21 17:09
 * @blame Java Team
 */
@Data
public class UserRegisterResponse extends AbstractResponse {
    /**
     * 发送激活邮件的邮箱
     */
    String email;
}
