package com.grasswort.picker.blog.dto.blog;

import lombok.Data;

/**
 * @author xuliangliang
 * @Classname BlogItemWithMarkdown
 * @Description 获取 Blog 内容
 * @Date 2019/10/31 18:28
 * @blame Java Team
 */
@Data
public class BlogItemWithMarkdown extends BlogItem {

    private String markdown;

}
