package com.grasswort.picker.blog.dto;

import com.grasswort.picker.commons.result.AbstractResponse;
import lombok.Data;

import java.util.List;

/**
 * @author xuliangliang
 * @Classname QueryBlogCategoryResponse
 * @Description 查看所有博客分类
 * @Date 2019/10/21 10:38
 * @blame Java Team
 */
@Data
public class QueryBlogCategoryResponse extends AbstractResponse {

    private List<Category> categorys;

    @Data
    public static class Category {

        private Long parentId;

        private Long categoryId;

        private String category;

        private List<Category> subCategorys;
    }
}
