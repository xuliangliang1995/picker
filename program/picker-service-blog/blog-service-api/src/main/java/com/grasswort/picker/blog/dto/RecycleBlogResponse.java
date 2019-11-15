package com.grasswort.picker.blog.dto;

import com.grasswort.picker.blog.constant.SysRetCodeConstants;
import com.grasswort.picker.commons.result.AbstractResponse;
import lombok.Data;

/**
 * @author xuliangliang
 * @Classname RecycleBlogResponse
 * @Description 回收博客
 * @Date 2019/11/6 22:32
 * @blame Java Team
 */
@Data
public class RecycleBlogResponse extends AbstractResponse {

    @Override
    public boolean isSuccess() {
        return SysRetCodeConstants.SUCCESS.getCode().equals(this.getCode());
    }
}
