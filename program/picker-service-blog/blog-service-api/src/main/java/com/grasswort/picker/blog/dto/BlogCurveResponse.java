package com.grasswort.picker.blog.dto;

import com.grasswort.picker.blog.constant.SysRetCodeConstants;
import com.grasswort.picker.commons.result.AbstractResponse;

/**
 * @author xuliangliang
 * @Classname BlogCurveResponse
 * @Description
 * @Date 2019/11/10 12:45
 * @blame Java Team
 */
public class BlogCurveResponse extends AbstractResponse {

    @Override
    public boolean isSuccess() {
        return SysRetCodeConstants.SUCCESS.getCode().equals(this.getCode());
    }
}
