package com.grasswort.picker.blog.constant;

/**
 * @author xuliangliang
 * @Classname TopicMenuTypeEnum
 * @Description 专题菜单类型
 * @Date 2019/12/11 13:53
 * @blame Java Team
 */
public enum TopicMenuTypeEnum {
    LINK(1, "link", "链接"),
    GROUP(2, "group", "分组"),
    FOLDER(3, "folder", "菜单");

    private int id;
    private String type;
    private String note;

    TopicMenuTypeEnum(int id, String type, String note) {
        this.id = id;
        this.type = type;
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getNote() {
        return note;
    }
}
