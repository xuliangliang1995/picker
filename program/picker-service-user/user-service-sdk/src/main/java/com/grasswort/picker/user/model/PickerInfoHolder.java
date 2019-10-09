package com.grasswort.picker.user.model;

/**
 * @author xuliangliang
 * @Classname PickInfoHolder
 * @Description PickerInfoHolder
 * @Date 2019/10/8 16:02
 * @blame Java Team
 */
public class PickerInfoHolder {

    private final static ThreadLocal<PickerInfo> PICKER_INFO = new ThreadLocal<>();

    public static PickerInfo getPickerInfo() {
        return PICKER_INFO.get();
    }

    public static void setPickerInfo(PickerInfo pickerInfo) {
        PICKER_INFO.set(pickerInfo);
    }
}
