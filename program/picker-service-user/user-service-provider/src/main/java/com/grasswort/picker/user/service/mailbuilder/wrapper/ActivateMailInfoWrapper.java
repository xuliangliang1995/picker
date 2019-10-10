package com.grasswort.picker.user.service.mailbuilder.wrapper;

import lombok.Builder;
import lombok.Getter;

/**
 * @author xuliangliang
 * @Classname ActivateMailInfoWrapper
 * @Description 激活邮件
 * @Date 2019/10/10 18:24
 * @blame Java Team
 */
@Builder
@Getter
public class ActivateMailInfoWrapper extends AbstractMailInfoWrapper {
    private String username;

    private String activateCode;

    private Long activateId;

}
