package com.grasswort.picker.blog.vo;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author xuliangliang
 * @Classname RecycleBinForm
 * @Description 回收站
 * @Date 2019/12/5 14:04
 * @blame Java Team
 */
@Data
public class RecycleBinForm {
    @Min(1)
    @NotNull
    private Integer pageNo;
    @Max(100)
    @NotNull
    private Integer pageSize;
}
