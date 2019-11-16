package com.grasswort.picker.blog.dto;

import com.grasswort.picker.blog.constant.SysRetCodeConstants;
import com.grasswort.picker.commons.result.AbstractResponse;

/**
 * @author xuliangliang
 * @Classname AbstractBlogResponse
 * @Description 抽象返回类
 * @Date 2019/11/16 15:15
 * @blame Java Team
 */
public abstract class AbstractBlogResponse extends AbstractResponse {

    @Override
    public boolean isSuccess() {
        return SysRetCodeConstants.SUCCESS.getCode().equals(this.getCode());
    }
}
