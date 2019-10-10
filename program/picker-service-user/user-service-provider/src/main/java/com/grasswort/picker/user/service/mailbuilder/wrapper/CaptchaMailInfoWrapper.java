package com.grasswort.picker.user.service.mailbuilder.wrapper;

import lombok.Builder;
import lombok.Getter;

/**
 * @author xuliangliang
 * @Classname CaptchaMailInfoWrapper
 * @Description 验证码邮件
 * @Date 2019/10/10 17:39
 * @blame Java Team
 */
@Builder
@Getter
public class CaptchaMailInfoWrapper extends AbstractMailInfoWrapper{

    private String name;

    private String captcha;

}
