package com.grasswort.picker.blog;

import com.grasswort.picker.blog.dto.BlogBrowseRequest;
import com.grasswort.picker.blog.dto.BlogBrowseResponse;

/**
 * @author xuliangliang
 * @Classname IBlogBrowseService
 * @Description 记录博客浏览事件
 * @Date 2019/11/25 16:55
 * @blame Java Team
 */
public interface IBlogBrowseService {
    /**
     * 博客浏览
     * @param blogBrowseRequest
     * @return
     */
    BlogBrowseResponse browse(BlogBrowseRequest blogBrowseRequest);
}
