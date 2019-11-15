package com.grasswort.picker.blog.dto;

import com.grasswort.picker.blog.constant.SysRetCodeConstants;
import com.grasswort.picker.commons.result.AbstractResponse;

/**
 * @author xuliangliang
 * @Classname DeleteBlogResponse
 * @Description 删除博客请求
 * @Date 2019/11/6 19:48
 * @blame Java Team
 */
public class DeleteBlogResponse extends AbstractResponse {

    @Override
    public boolean isSuccess() {
        return SysRetCodeConstants.SUCCESS.getCode().equals(this.getCode());
    }
}
