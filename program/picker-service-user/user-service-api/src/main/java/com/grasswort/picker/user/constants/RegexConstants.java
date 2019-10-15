package com.grasswort.picker.user.constants;

/**
 * @author xuliangliang
 * @Classname RegexConstants
 * @Description 正则表达式
 * @Date 2019/10/15 16:01
 * @blame Java Team
 */
public class RegexConstants {
    /**
     * 用户名正则
     */
    public static final String USERNAME_REGEX = "^[a-zA-Z0-9]{8,20}$";
    /**
     * 密码正则
     */
    public static final String PASSWORD_REGEX = "^[a-zA-Z0-9]{8,20}$";
    /**
     * 手机号正则
     */
    public static final String MOBILE_REGEX = "^1(3[0-9]|4[5,7]|5[0,1,2,3,5,6,7,8,9]|6[2,5,6,7]|7[0,1,7,8]|8[0-9]|9[1,8,9])\\d{8}$";
}
