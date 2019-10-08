package com.grasswort.picker.user.constants;

/**
 * @author xuliangliang
 * @Classname SysRetCodeConstants
 * @Description user-service 统一错误码： 003
 * @Date 2019/9/22 9:30
 * @blame Java Team
 */
public enum SysRetCodeConstants {
    SUCCESS                             ("000000", "成功"),

    USER_OR_PASSWORD_ERROR               ("003001","用户名或密码不正确"),
    TOKEN_VALID_FAILED                  ("003002","token校验失败"),
    USERNAME_ALREADY_EXISTS             ("003003","用户名已存在"),
    USER_REGISTER_FAILED                ("003004","注册失败，请联系管理员"),
    SECURITY_CODE_ERROR                  ("003005","验证码不正确"),
    USER_IS_VERIFIED_ERROR                ("003006","用户名尚未激活"),
    USER_REGISTER_VERIFY_FAILED         ("003007","用户注册失败插入验证数据失败"),
    ACTIVATE_URL_LOSE_EFFICACY         ("003008","账号激活链接已失效"),

    REQUEST_FORMAT_ILLEGAL              ("003060", "请求数据格式非法"),
    REQUEST_IP_ILLEGAL                  ("003061", "请求IP非法"),
    REQUEST_CHECK_FAILURE               ("003062", "请求数据校验失败"),
    DATA_NOT_EXIST                      ("003070", "数据不存在"),
    DATA_REPEATED                       ("003071", "数据重复"),
    REQUEST_DATA_NOT_EXIST              ("003072", "传入对象不能为空"),
    REQUEST_DATA_ERROR                  ("003074", "必要的参数不正确"),
    REQUISITE_PARAMETER_NOT_EXIST       ("003073", "必要的参数不能为空"),
    PERMISSION_DENIED                   ("003091", "权限不足"),
    DB_EXCEPTION                        ("003097", "数据库异常"),
    SYSTEM_TIMEOUT                      ("003098", "系统超时"),
    SYSTEM_ERROR                        ("003099", "系统错误"),

    USER_VERIFY_INFO_INVALID            ("003200", "用户注册验证验证信息不合法");
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
