package com.grasswort.picker.blog;

import com.grasswort.picker.blog.dto.ChangeBlogCategoryRequest;
import com.grasswort.picker.blog.dto.ChangeBlogCategoryResponse;
import com.grasswort.picker.blog.dto.CreateBlogRequest;
import com.grasswort.picker.blog.dto.CreateBlogResponse;

/**
 * @author xuliangliang
 * @Classname IBlogEditService
 * @Description 博客编辑服务（包括创建）
 * @Date 2019/10/19 12:35
 * @blame Java Team
 */
public interface IBlogEditService {

    /**
     * 创建博客
     * @param blogRequest
     * @return
     */
    CreateBlogResponse createBlog(CreateBlogRequest blogRequest);

    /**
     * 修改博客分类
     * @param changeBlogCategoryRequest
     * @return
     */
    ChangeBlogCategoryResponse changeBlogCategory(ChangeBlogCategoryRequest changeBlogCategoryRequest);
}
