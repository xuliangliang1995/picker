package com.grasswort.picker.blog.constant;

/**
 * @author xuliangliang
 * @Classname SysRetCodeConstants
 * @Description 系统返回码
 * @Date 2019/10/21 9:41
 * @blame Java Team
 */
public enum SysRetCodeConstants {
    // 通用
    SUCCESS                             ("000000", "成功"),
    DB_EXCEPTION                        ("003003", "数据库异常"),
    SYSTEM_TIMEOUT                      ("003004", "系统超时"),
    SYSTEM_ERROR                        ("003005", "系统错误"),
    PLEASE_RETRY                        ("003006", "操作失败，请重试"),

    BLOG_CATEGORY_NOT_EXISTS            ("001001", "博客类别不存在"),
    BLOG_CATEGORY_EXISTS                ("001002", "博客类别已存在"),
    BLOG_PARENT_CATEGORY_NOT_EXISTS     ("001003", "所属父类不存在"),
    BLOG_CATEGORY_NOT_EMPTY             ("001004", "非空文件夹不允许删除"),

    BLOG_NOT_EXISTS                 ("002001", "博客不存在"),

    CURVE_NOT_EXISTS                 ("003001", "记忆曲线节点不存在"),

    COMMENT_NOT_EXISTS              ("004001", "目标评论不存在或已被删除"),

    PARENT_MENU_NOT_EXISTS          ("005001", "父级菜单不存在"),
    MENU_LEVEL_TOO_HIGH            ("005002", "菜单层级过高"),
    MENU_NOT_VALID                 ("005003", "菜单创建不规范"),

    TOPIC_NOT_EXISTS                ("006001", "专题不存在");

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
