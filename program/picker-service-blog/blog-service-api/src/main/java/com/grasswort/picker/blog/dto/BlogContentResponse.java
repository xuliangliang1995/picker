package com.grasswort.picker.blog.dto;

import com.grasswort.picker.blog.constant.SysRetCodeConstants;
import com.grasswort.picker.commons.result.AbstractResponse;
import lombok.Data;

/**
 * @author xuliangliang
 * @Classname BlogContentResponse
 * @Description 博客
 * @Date 2019/10/19 13:15
 * @blame Java Team
 */
@Data
public class BlogContentResponse extends AbstractResponse {

    private String markdown;

    @Override
    public boolean isSuccess() {
        return SysRetCodeConstants.SUCCESS.getCode().equals(this.getCode());
    }

}
