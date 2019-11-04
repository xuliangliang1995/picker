package com.grasswort.picker.user.constants;

/**
 * @author xuliangliang
 * @Classname SafetyCheckMode
 * @Description 安全检测方式
 * @Date 2019/11/4 16:05
 * @blame Java Team
 */
public enum SafetyCheckMode {
    EMAIL(0, "邮件"),
    SMS(1, "短信");
    private int id;
    private String note;

    SafetyCheckMode(int id, String note) {
        this.id = id;
        this.note = note;
    }

    public int getId() {
        return id;
    }
}
