package com.grasswort.picker.user.vo;

import lombok.Data;

import javax.validation.constraints.Size;

/**
 * @author xuliangliang
 * @Classname EditGithubForm.java
 * @Description
 * @Date 2019/12/31
 * @blame Java Team
 */
@Data
public class EditGithubForm {
    @Size(max = 45)
    private String github;

}
