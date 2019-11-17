package com.grasswort.picker.user.constants;

/**
 * @author xuliangliang
 * @Classname BlogPushMode
 * @Description 博客推送方式
 * @Date 2019/11/17 13:20
 * @blame Java Team
 */
public enum BlogPushMode {
    EMAIL(0, "邮件"),
    WX(1, "微信");
    private int id;
    private String note;

    BlogPushMode(int id, String note) {
        this.id = id;
        this.note = note;
    }

    public int getId() {
        return id;
    }
}
