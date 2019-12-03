package com.grasswort.picker.user.vo;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author xuliangliang
 * @Classname SearchUserForm
 * @Description 用户查询表单
 * @Date 2019/12/3 23:24
 * @blame Java Team
 */
@Data
public class SearchUserForm {

    private String keyword;
    @Min(1)
    @NotNull
    private Integer pageNo;
    @NotNull
    @Max(100)
    private Integer pageSize;
}
