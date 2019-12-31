package com.grasswort.picker.user.vo;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * @author xuliangliang
 * @Classname EditIntroForm.java
 * @Description
 * @Date 2019/12/31
 * @blame Java Team
 */
@Data
public class EditIntroForm {
    @NotEmpty
    @Size(max = 500)
    private String intro;
}
