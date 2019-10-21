package com.grasswort.picker.blog.service;

import com.grasswort.picker.blog.IBlogEditService;
import com.grasswort.picker.blog.constant.DBGroup;
import com.grasswort.picker.blog.constant.SysRetCodeConstants;
import com.grasswort.picker.blog.dao.entity.Blog;
import com.grasswort.picker.blog.dao.entity.BlogContent;
import com.grasswort.picker.blog.dao.persistence.BlogContentMapper;
import com.grasswort.picker.blog.dao.persistence.BlogMapper;
import com.grasswort.picker.blog.dao.persistence.ext.BlogCategoryDao;
import com.grasswort.picker.blog.dto.CreateBlogRequest;
import com.grasswort.picker.blog.dto.CreateBlogResponse;
import com.grasswort.picker.commons.annotation.DB;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Objects;

/**
 * @author xuliangliang
 * @Classname BlogServiceEditServiceImpl
 * @Description 博客服务编辑、创建
 * @Date 2019/10/21 9:15
 * @blame Java Team
 */
@Service(version = "1.0")
public class BlogServiceEditServiceImpl implements IBlogEditService {

    @Autowired BlogMapper blogMapper;

    @Autowired BlogContentMapper blogContentMapper;

    @Autowired BlogCategoryDao blogCategoryDao;

    /**
     * 创建博客
     *
     * @param blogRequest
     * @return
     */
    @Override
    @DB(DBGroup.MASTER)
    @Transactional(rollbackFor = Exception.class)
    public CreateBlogResponse createBlog(CreateBlogRequest blogRequest) {
        CreateBlogResponse createBlogResponse = new CreateBlogResponse();
        final int FIRST_EDITION = 1;

        String markdown = blogRequest.getMarkdown();
        String html = blogRequest.getHtml();
        Long userId = blogRequest.getUserId();
        Long categoryId = blogRequest.getCategoryId();

        Long categoryOwnerId = blogCategoryDao.selectUserIdByPrimaryKey(categoryId);
        boolean categoryNotExists =  categoryOwnerId == null || ! Objects.equals(categoryOwnerId, userId);
        if (categoryNotExists) {
            createBlogResponse.setCode(SysRetCodeConstants.BLOG_CATEGORY_NOT_EXISTS.getCode());
            createBlogResponse.setMsg(SysRetCodeConstants.BLOG_CATEGORY_NOT_EXISTS.getMsg());
            return createBlogResponse;
        }

        Blog blog = new Blog();
        blog.setPkUserId(userId);
        blog.setVersion(FIRST_EDITION);
        blog.setCategoryId(categoryId == null ? 0 : categoryId);
        Date now = new Date(System.currentTimeMillis());
        blog.setGmtCreate(now);
        blog.setGmtModified(now);
        blogMapper.insertUseGeneratedKeys(blog);

        BlogContent blogContent = new BlogContent();
        blogContent.setBlogId(blog.getId());
        blogContent.setBlogVersion(FIRST_EDITION);
        blogContent.setMarkdown(markdown);
        blogContent.setHtml(html);
        blogContent.setGmtCreate(now);
        blogContent.setGmtModified(now);
        blogContentMapper.insertUseGeneratedKeys(blogContent);

        createBlogResponse.setCode(SysRetCodeConstants.SUCCESS.getCode());
        createBlogResponse.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
        return createBlogResponse;
    }
}
