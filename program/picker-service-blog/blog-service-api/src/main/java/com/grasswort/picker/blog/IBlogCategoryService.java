package com.grasswort.picker.blog;

import com.grasswort.picker.blog.dto.*;

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

    /**
     * 修改博客分类
     * @param editCategoryRequest
     * @return
     */
    EditCategoryResponse editCategory(EditCategoryRequest editCategoryRequest);

    /**
     * 删除博客分类
     * @param deleteCategoryRequest
     * @return
     */
    DeleteCategoryResponse deleteCategory(DeleteCategoryRequest deleteCategoryRequest);
}
