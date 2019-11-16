package com.grasswort.picker.blog;

import com.grasswort.picker.blog.dto.*;

/**
 * @author xuliangliang
 * @Classname IBlogService
 * @Description 博客查看服务
 * @Date 2019/10/19 12:28
 * @blame Java Team
 */
public interface IBlogService {
    /**
     * 查看自己的博客列表
     * @param ownBlogListRequest
     * @return
     */
    OwnBlogListResponse ownBlogList(OwnBlogListRequest ownBlogListRequest);

    /**
     * 获取博客 markdown 内容
     * @param markdownRequest
     * @return
     */
    BlogMarkdownResponse markdown(BlogMarkdownRequest markdownRequest);

    /**
     * 获取博客 HTML 内容
     * @param htmlRequest
     * @return
     */
    BlogHtmlResponse html(BlogHtmlRequest htmlRequest);

}
