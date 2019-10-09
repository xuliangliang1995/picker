package com.grasswort.picker.user.constants;

/**
 * @author xuliangliang
 * @Classname SexEnum
 * @Description 性别
 * @Date 2019/10/9 11:58
 * @blame Java Team
 */
public enum SexEnum {
    PRIVARY((byte) 0, "保密"),
    MALE((byte) 1, "男"),
    FEMALE((byte) 2, "女");

    SexEnum(byte value, String sex) {
        this.value = value;
        this.sex = sex;
    }

    private byte value;

    private String sex;

    public byte getValue() {
        return value;
    }

    public String getSex() {
        return sex;
    }
}
