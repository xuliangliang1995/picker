package com.grasswort.picker.wechat.constants;

/**
 * @author xuliangliang
 * @Classname SysRetCodeConstants
 * @Description user-service 统一错误码： 003
 * @Date 2019/9/22 9:30
 * @blame Java Team
 */
public enum SysRetCodeConstants {
    // 通用
    SUCCESS                             ("000000", "成功"),
    INVALID_REQUEST                     ("001001", "非法请求");

    private String code;
    private String msg;

    SysRetCodeConstants(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
