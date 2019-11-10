package com.grasswort.picker.blog.constant;

/**
 * @author xuliangliang
 * @Classname BlogCurveStatusEnum
 * @Description 博客曲线状态
 * @Date 2019/11/10 13:27
 * @blame Java Team
 */
public enum BlogCurveStatusEnum {
    NORMAL(0, "正常"),
    STOP(1, "停止");

    BlogCurveStatusEnum(int status, String note) {
        this.status = status;
        this.note = note;
    }

    private int status;

    private String note;

    public int status() {
        return status;
    }

}
