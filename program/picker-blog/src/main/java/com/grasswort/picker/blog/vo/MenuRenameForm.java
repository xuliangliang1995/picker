package com.grasswort.picker.blog.vo;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * @author xuliangliang
 * @Classname MenuRenameForm
 * @Description 菜单重命名
 * @Date 2019/12/12 15:15
 * @blame Java Team
 */
@Data
public class MenuRenameForm {
    @NotEmpty
    @Size(max = 50)
    private String menuName;
}
