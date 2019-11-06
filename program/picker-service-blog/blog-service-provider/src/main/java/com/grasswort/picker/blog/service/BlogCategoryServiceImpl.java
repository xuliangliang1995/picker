package com.grasswort.picker.blog.service;

import com.grasswort.picker.blog.IBlogCategoryService;
import com.grasswort.picker.blog.constant.DBGroup;
import com.grasswort.picker.blog.constant.SysRetCodeConstants;
import com.grasswort.picker.blog.dao.entity.BlogCategory;
import com.grasswort.picker.blog.dao.persistence.BlogCategoryMapper;
import com.grasswort.picker.blog.dao.persistence.ext.BlogCategoryDao;
import com.grasswort.picker.blog.dto.*;
import com.grasswort.picker.commons.annotation.DB;
import com.grasswort.picker.commons.config.DBLocalHolder;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
     * @param createRequest
     * @return
     */
    @Override
    @DB(DBGroup.MASTER)
    public CreateBlogCategoryResponse createCategory(CreateBlogCategoryRequest createRequest) {
        CreateBlogCategoryResponse createBlogCategoryResponse= new CreateBlogCategoryResponse();

        Long userId = createRequest.getUserId();
        Long parentId = createRequest.getParentId();
        String category = createRequest.getCategory();
        Long categoryId = blogCategoryDao.selectIdByPkUserIdAndCategory(userId, parentId, category);
        boolean categoryExists = categoryId != null;
        if (categoryExists) {
            createBlogCategoryResponse.setCode(SysRetCodeConstants.BLOG_CATEGORY_EXISTS.getCode());
            createBlogCategoryResponse.setMsg(SysRetCodeConstants.BLOG_CATEGORY_EXISTS.getMsg());
            return createBlogCategoryResponse;
        }

        if (createRequest.getParentId() > 0) {
            boolean parentExists = blogCategoryDao.existsWithPrimaryKey(createRequest.getParentId());
            if (! parentExists) {
                createBlogCategoryResponse.setCode(SysRetCodeConstants.BLOG_PARENT_CATEGORY_NOT_EXISTS.getCode());
                createBlogCategoryResponse.setMsg(SysRetCodeConstants.BLOG_PARENT_CATEGORY_NOT_EXISTS.getMsg());
                return createBlogCategoryResponse;
            }
        }

        BlogCategory blogCategory = new BlogCategory();
        blogCategory.setPkUserId(userId);
        blogCategory.setParentId(createRequest.getParentId());
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
        example.createCriteria().andEqualTo("pkUserId", userId).andEqualTo("parentId", 0);

        List<BlogCategory> categories = blogCategoryMapper.selectByExample(example);
        categoryResponse.setCategorys(
                categories.stream().map(category -> convertCategory(category)).collect(Collectors.toList())
        );

        categoryResponse.setCode(SysRetCodeConstants.SUCCESS.getCode());
        categoryResponse.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
        return categoryResponse;
    }

    /**
     * 修改博客
     * @param editCategoryRequest
     * @return
     */
    @Override
    @DB(DBGroup.SLAVE)
    public EditCategoryResponse editCategory(EditCategoryRequest editCategoryRequest) {
        EditCategoryResponse editCategoryResponse = new EditCategoryResponse();

        Long categoryId = editCategoryRequest.getCategoryId();
        Long userId = editCategoryRequest.getUserId();
        Long parentId = editCategoryRequest.getParentId();
        String  category = editCategoryRequest.getCategory();

        BlogCategory blogCategory = Optional.ofNullable(blogCategoryMapper.selectByPrimaryKey(categoryId))
                .filter(c -> c.getPkUserId().equals(userId))
                .orElse(null);

        boolean categoryNotExists = blogCategory == null;
        if (categoryNotExists) {
            editCategoryResponse.setCode(SysRetCodeConstants.BLOG_CATEGORY_NOT_EXISTS.getCode());
            editCategoryResponse.setMsg(SysRetCodeConstants.BLOG_CATEGORY_NOT_EXISTS.getMsg());
            return editCategoryResponse;
        }

        if (parentId == null) {
            parentId = blogCategory.getParentId();
        }
        if (StringUtils.isBlank(category)) {
            category = blogCategory.getCategory();
        }
        if (Objects.equals(parentId, blogCategory.getParentId()) && Objects.equals(category, blogCategory.getCategory())) {
            editCategoryResponse.setCode(SysRetCodeConstants.SUCCESS.getCode());
            editCategoryResponse.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
            return editCategoryResponse;
        }

        boolean targetCategoryExists = blogCategoryDao.selectIdByPkUserIdAndCategory(userId, parentId, category) != null;
        if (targetCategoryExists) {
            editCategoryResponse.setCode(SysRetCodeConstants.BLOG_CATEGORY_EXISTS.getCode());
            editCategoryResponse.setMsg(SysRetCodeConstants.BLOG_CATEGORY_EXISTS.getMsg());
            return editCategoryResponse;
        }

        BlogCategory categorySelective = new BlogCategory();
        categorySelective.setId(blogCategory.getId());
        categorySelective.setParentId(parentId);
        categorySelective.setCategory(category);
        categorySelective.setGmtModified(new Date(System.currentTimeMillis()));

        DBLocalHolder.selectDBGroup(DBGroup.MASTER);
        blogCategoryMapper.updateByPrimaryKeySelective(categorySelective);

        editCategoryResponse.setCode(SysRetCodeConstants.SUCCESS.getCode());
        editCategoryResponse.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
        return editCategoryResponse;
    }

    /**
     * category converter
     * @param blogCategory
     * @return
     */
    private QueryBlogCategoryResponse.Category convertCategory(BlogCategory blogCategory) {
        QueryBlogCategoryResponse.Category c = new QueryBlogCategoryResponse.Category();
        c.setCategoryId(blogCategory.getId());
        c.setCategory(blogCategory.getCategory());
        c.setParentId(blogCategory.getParentId());

        Example ex = new Example(BlogCategory.class);
        ex.createCriteria().andEqualTo("parentId", blogCategory.getId());
        c.setSubCategorys(blogCategoryMapper.selectByExample(ex).stream().map(c1 -> convertCategory(c1)).collect(Collectors.toList()));
        return c;
    }
}
