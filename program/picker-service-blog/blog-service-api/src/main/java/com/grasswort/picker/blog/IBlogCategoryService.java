package com.grasswort.picker.blog;

import com.grasswort.picker.blog.dto.CreateBlogCategoryRequest;
import com.grasswort.picker.blog.dto.CreateBlogCategoryResponse;
import com.grasswort.picker.blog.dto.QueryBlogCategoryRequest;
import com.grasswort.picker.blog.dto.QueryBlogCategoryResponse;

/**
 * @author xuliangliang
 * @Classname IBlogCategoryService
 * @Description 博客分类
 * @Date 2019/10/21 9:56
 * @blame Java Team
 */
public interface IBlogCategoryService {
    /**
     * 创建博客分类
     * @param createRequest
     * @return
     */
    CreateBlogCategoryResponse createCategory(CreateBlogCategoryRequest createRequest);

    /**
     * 查看所有分类（自己的）
     * @param queryBlogCategoryRequest
     * @return
     */
    QueryBlogCategoryResponse categorys(QueryBlogCategoryRequest queryBlogCategoryRequest);
}
