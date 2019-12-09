package com.grasswort.picker.blog;

import com.grasswort.picker.blog.dto.BlogPoolQueryRequest;
import com.grasswort.picker.blog.dto.BlogPoolQueryResponse;

/**
 * @author xuliangliang
 * @Classname IBlogPoolService
 * @Description 专用查询的博客服务
 * @Date 2019/11/20 15:48
 * @blame Java Team
 */
public interface IBlogPoolService {
    /**
     * 获取博客列表
     * @param queryRequest
     * @return
     */
    BlogPoolQueryResponse blogPool(BlogPoolQueryRequest queryRequest);

    /**
     * 初始化
     */
    void init();

}
