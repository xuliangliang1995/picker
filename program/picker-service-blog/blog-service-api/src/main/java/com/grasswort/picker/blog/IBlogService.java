package com.grasswort.picker.blog;

import com.grasswort.picker.blog.dto.OwnBlogListRequest;
import com.grasswort.picker.blog.dto.OwnBlogListResponse;

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

}
