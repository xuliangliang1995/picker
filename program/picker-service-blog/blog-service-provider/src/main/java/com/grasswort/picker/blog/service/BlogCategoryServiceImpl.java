package com.grasswort.picker.blog.service;

import com.grasswort.picker.blog.IBlogCategoryService;
import com.grasswort.picker.blog.constant.DBGroup;
import com.grasswort.picker.blog.constant.SysRetCodeConstants;
import com.grasswort.picker.blog.dao.entity.BlogCategory;
import com.grasswort.picker.blog.dao.persistence.BlogCategoryMapper;
import com.grasswort.picker.blog.dao.persistence.ext.BlogCategoryDao;
import com.grasswort.picker.blog.dto.CreateBlogCategoryRequest;
import com.grasswort.picker.blog.dto.CreateBlogCategoryResponse;
import com.grasswort.picker.blog.dto.QueryBlogCategoryRequest;
import com.grasswort.picker.blog.dto.QueryBlogCategoryResponse;
import com.grasswort.picker.commons.annotation.DB;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xuliangliang
 * @Classname BlogCategoryServiceImpl
 * @Description 博客类别服务
 * @Date 2019/10/21 10:03
 * @blame Java Team
 */
@Service(version = "1.0")
public class BlogCategoryServiceImpl implements IBlogCategoryService {

    @Autowired BlogCategoryMapper blogCategoryMapper;

    @Autowired BlogCategoryDao blogCategoryDao;
    /**
     * 创建博客分类
     *
     * @param createRequest
     * @return
     */
    @Override
    @DB(DBGroup.MASTER)
    public CreateBlogCategoryResponse createCategory(CreateBlogCategoryRequest createRequest) {
        CreateBlogCategoryResponse createBlogCategoryResponse= new CreateBlogCategoryResponse();

        Long userId = createRequest.getUserId();
        String category = createRequest.getCategory();
        Long categoryId = blogCategoryDao.selectIdByPkUserIdAndCategory(userId, category);
        boolean categoryExists = categoryId != null;
        if (categoryExists) {
            createBlogCategoryResponse.setCode(SysRetCodeConstants.BLOG_CATEGORY_EXISTS.getCode());
            createBlogCategoryResponse.setMsg(SysRetCodeConstants.BLOG_CATEGORY_EXISTS.getMsg());
            return createBlogCategoryResponse;
        }

        BlogCategory blogCategory = new BlogCategory();
        blogCategory.setPkUserId(userId);
        blogCategory.setCategory(category);
        Date now = new Date(System.currentTimeMillis());
        blogCategory.setGmtCreate(now);
        blogCategory.setGmtModified(now);
        blogCategoryMapper.insertUseGeneratedKeys(blogCategory);

        createBlogCategoryResponse.setCode(SysRetCodeConstants.SUCCESS.getCode());
        createBlogCategoryResponse.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
        return createBlogCategoryResponse;
    }

    /**
     * 查看所有分类（自己的）
     *
     * @param queryBlogCategoryRequest
     * @return
     */
    @Override
    @DB(DBGroup.SLAVE)
    public QueryBlogCategoryResponse categorys(QueryBlogCategoryRequest queryBlogCategoryRequest) {
        QueryBlogCategoryResponse categoryResponse = new QueryBlogCategoryResponse();

        Long userId = queryBlogCategoryRequest.getUserId();
        Example example = new Example(BlogCategory.class);
        example.createCriteria().andEqualTo("pkUserId", userId);

        List<BlogCategory> categorys = blogCategoryMapper.selectByExample(example);
        categoryResponse.setCategorys(categorys.stream().map(category -> {
            QueryBlogCategoryResponse.Category c = new QueryBlogCategoryResponse.Category();
            c.setCategoryId(category.getId());
            c.setCategory(category.getCategory());
            return c;
        }).collect(Collectors.toList()));

        categoryResponse.setCode(SysRetCodeConstants.SUCCESS.getCode());
        categoryResponse.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
        return categoryResponse;
    }
}
