package com.grasswort.picker.user.vo;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author xuliangliang
 * @Classname AuthorForm
 * @Description
 * @Date 2019/11/21 15:33
 * @blame Java Team
 */
@Data
public class AuthorForm {
    @NotEmpty
    private String pickerId;
}
