package com.grasswort.picker.blog.dto;

import com.grasswort.picker.blog.constant.SysRetCodeConstants;
import com.grasswort.picker.blog.dto.blog.BlogItemWithMarkdown;
import com.grasswort.picker.commons.result.AbstractResponse;
import lombok.Data;

/**
 * @author xuliangliang
 * @Classname BlogMarkdownResponse
 * @Description 博客 markdown 响应
 * @Date 2019/10/31 18:19
 * @blame Java Team
 */
@Data
public class BlogMarkdownResponse extends AbstractResponse {

    private BlogItemWithMarkdown blog;

    @Override
    public boolean isSuccess() {
        return SysRetCodeConstants.SUCCESS.getCode().equals(this.getCode());
    }

}
