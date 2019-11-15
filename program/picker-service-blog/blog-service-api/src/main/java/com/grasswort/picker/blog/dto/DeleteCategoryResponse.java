package com.grasswort.picker.blog.dto;

import com.grasswort.picker.blog.constant.SysRetCodeConstants;
import com.grasswort.picker.commons.result.AbstractResponse;

/**
 * @author xuliangliang
 * @Classname DeleteCategoryResponse
 * @Description 删除
 * @Date 2019/11/7 15:50
 * @blame Java Team
 */
public class DeleteCategoryResponse extends AbstractResponse {

    @Override
    public boolean isSuccess() {
        return SysRetCodeConstants.SUCCESS.getCode().equals(this.getCode());
    }
}
