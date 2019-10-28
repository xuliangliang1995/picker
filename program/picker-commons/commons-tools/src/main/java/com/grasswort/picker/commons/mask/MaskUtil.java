package com.grasswort.picker.commons.mask;

import org.apache.commons.lang3.StringUtils;

/**
 * @author xuliangliang
 * @Classname MaskUtil
 * @Description 数据脱敏
 * @Date 2019/10/15 12:49
 * @blame Java Team
 */
public class MaskUtil {
    /**
     * 隐藏手机号
     *
     * @param mobile
     * @return
     */
    public static String maskMobile(String mobile) {
        if (StringUtils.isEmpty(mobile) || (mobile.length() != 11)) {
            return mobile;
        }
        return mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }

    /**
     * 隐藏邮箱信息
     *
     * @param email
     * @return
     */
    public static String maskEmail(String email) {
        if (StringUtils.isEmpty(email)) {
            return email;
        }
        return email.replaceAll("(\\w+)\\w{3}@(\\w+)", "$1***@$2");
    }

    /**
     * 隐藏身份证号码
     *
     * @param id
     * @return
     */
    public static String maskIdCardNo(String id) {
        if (StringUtils.isEmpty(id) || (id.length() < 8)) {
            return id;
        }
        return id.replaceAll("(?<=\\w{6})\\w(?=\\w{6})", "*");
    }

}
