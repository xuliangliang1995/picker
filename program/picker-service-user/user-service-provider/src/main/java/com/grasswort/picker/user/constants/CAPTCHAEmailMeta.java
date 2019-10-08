package com.grasswort.picker.user.constants;

import com.grasswort.picker.email.constants.EmailCenterConstant;

/**
 * @author xuliangliang
 * @Classname CAPTCHAEmailConstants
 * @Description TODO
 * @Date 2019/10/8 23:26
 * @blame Java Team
 */
public class CAPTCHAEmailMeta {
    public static final String CAPTCHA_EMAIL_TOPIC = EmailCenterConstant.TOPIC_EMAIL_PREFIX.concat("user-captcha-email");

    public static final int PATITIONS = 5;

    public static final short REPLICATION_FACTOR = 2;

    public static final String SUBJECT = "Picker 验证码";
}
