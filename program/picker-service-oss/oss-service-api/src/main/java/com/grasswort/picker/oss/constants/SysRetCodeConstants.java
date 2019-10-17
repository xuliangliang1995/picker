package com.grasswort.picker.oss.constants;

/**
 * @author xuliangliang
 * @Classname SysRetCodeConstants
 * @Description oss-service 统一错误码： 003
 * @Date 2019/10/17 22:57
 * @blame Java Team
 */
public enum SysRetCodeConstants {
    // 通用
    SUCCESS                             ("000000", "成功"),
    DB_EXCEPTION                        ("003003", "数据库异常"),
    SYSTEM_TIMEOUT                      ("003004", "系统超时"),
    SYSTEM_ERROR                        ("003005", "系统错误"),
    PLEASE_RETRY                        ("003006", "操作失败，请重试");

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
