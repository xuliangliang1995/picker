package com.grasswort.picker.blog.dto;

import com.grasswort.picker.blog.constant.SysRetCodeConstants;
import com.grasswort.picker.commons.result.AbstractResponse;

/**
 * @author xuliangliang
 * @Classname ChangeCategoryParentResponse
 * @Description 修改博客分类所属目录
 * @Date 2019/11/6 17:23
 * @blame Java Team
 */
public class EditCategoryResponse extends AbstractResponse {

    @Override
    public boolean isSuccess() {
        return SysRetCodeConstants.SUCCESS.getCode().equals(this.getCode());
    }
}
