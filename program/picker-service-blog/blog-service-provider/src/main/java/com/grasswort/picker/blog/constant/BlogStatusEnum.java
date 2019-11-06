package com.grasswort.picker.blog.constant;

/**
 * @author xuliangliang
 * @Classname BlogStatusEnum
 * @Description 博客状态
 * @Date 2019/11/6 22:03
 * @blame Java Team
 */
public enum BlogStatusEnum {
    NORMAL(0, "正常"),
    RECOVERABLE(1, "回收中"),
    DELETE(2, "已删除");

    BlogStatusEnum(int status, String note) {
        this.status = status;
        this.note = note;
    }

    private int status;

    private String note;

    public int status() {
        return status;
    }
}
