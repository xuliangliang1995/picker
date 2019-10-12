package com.grasswort.picker.user.model;

import lombok.Builder;
import lombok.Data;

/**
 * @author xuliangliang
 * @Classname PickerInfo
 * @Description Picker 基本信息
 * @Date 2019/10/8 15:58
 * @blame Java Team
 */
@Data
@Builder
public class PickerInfo {

    private Long id;

    private String name;

    private boolean privilege;

}
