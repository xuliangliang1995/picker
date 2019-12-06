package com.grasswort.picker.user.vo;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author xuliangliang
 * @Classname PageForm
 * @Description
 * @Date 2019/12/6 15:26
 * @blame Java Team
 */
@Data
public class PageForm {
    @NotNull
    @Min(1)
    private Integer pageNo;
    @NotNull
    @Min(1)
    @Max(100)
    private Integer pageSize;
}
