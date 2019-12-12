package com.grasswort.picker.blog.constant;

/**
 * @author xuliangliang
 * @Classname TopicStatusEnum
 * @Description
 * @Date 2019/12/12 14:31
 * @blame Java Team
 */
public enum TopicStatusEnum {
    PRIVATE(0, "私密"),
    PUBLIC(1, "公开");

    private int status;
    private String note;

    TopicStatusEnum(int status, String note) {
        this.status = status;
        this.note = note;
    }

    public int getStatus() {
        return status;
    }

    public String getNote() {
        return note;
    }
}
