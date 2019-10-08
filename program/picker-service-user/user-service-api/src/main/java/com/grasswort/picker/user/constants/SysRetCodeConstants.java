package com.grasswort.picker.user.constants;

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
    SECURITY_CODE_ERROR                  ("003001","验证码不正确"),
    PERMISSION_DENIED                   ("003002", "权限不足"),
    DB_EXCEPTION                        ("003003", "数据库异常"),
    SYSTEM_TIMEOUT                      ("003004", "系统超时"),
    SYSTEM_ERROR                        ("003005", "系统错误"),

    // 注册
    USERNAME_ALREADY_EXISTS             ("003101","用户名已存在"),
    USER_REGISTER_FAILED                ("003102","注册失败，请联系管理员"),
    // 激活
    USERNAME_NOT_EXISTS                 ("003201", "用户名不存在"),
    ACTIVATE_URL_LOSE_EFFICACY         ("003202","账户激活链接已失效"),
    USERNAME_IS_ACTIVATED               ("003203", "账户已激活"),
    // 登录
    USER_OR_PASSWORD_ERROR               ("003301","用户名或密码不正确"),
    TOKEN_VALID_FAILED                  ("003302","token校验失败"),
    USER_IS_VERIFIED_ERROR                ("003303","用户名尚未激活");

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
