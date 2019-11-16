package com.grasswort.picker.user.constants;

/**
 * @author xuliangliang
 * @Classname CAPTCHAReceiver
 * @Description 验证码接收者
 * @Date 2019/10/8 23:07
 * @blame Java Team
 */
public enum CAPTCHAReceiver {
    PHONE(1, "手机"),
    EMAIL(2, "邮箱"),
    WECHAT(3, "微信");

    CAPTCHAReceiver(int id, String type) {
        this.id = id;
        this.type = type;
    }

    private int id;

    private String type;

    public int getId() {
        return id;
    }
}
