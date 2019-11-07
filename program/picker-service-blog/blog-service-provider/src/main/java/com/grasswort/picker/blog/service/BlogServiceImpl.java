package com.grasswort.picker.blog.service;

import com.grasswort.picker.blog.IBlogService;
import com.grasswort.picker.blog.constant.BlogStatusEnum;
import com.grasswort.picker.blog.constant.DBGroup;
import com.grasswort.picker.blog.constant.SysRetCodeConstants;
import com.grasswort.picker.blog.dao.entity.Blog;
import com.grasswort.picker.blog.dao.entity.BlogCategory;
import com.grasswort.picker.blog.dao.persistence.BlogCategoryMapper;
import com.grasswort.picker.blog.dao.persistence.BlogLabelMapper;
import com.grasswort.picker.blog.dao.persistence.BlogMapper;
import com.grasswort.picker.blog.dao.persistence.ext.BlogContentDao;
import com.grasswort.picker.blog.dao.persistence.ext.BlogLabelDao;
import com.grasswort.picker.blog.dto.BlogMarkdownRequest;
import com.grasswort.picker.blog.dto.BlogMarkdownResponse;
import com.grasswort.picker.blog.dto.OwnBlogListRequest;
import com.grasswort.picker.blog.dto.OwnBlogListResponse;
import com.grasswort.picker.blog.dto.blog.BlogItem;
import com.grasswort.picker.blog.dto.blog.BlogItemWithMarkdown;
import com.grasswort.picker.blog.util.BlogIdEncrypt;
import com.grasswort.picker.commons.annotation.DB;
import com.grasswort.picker.commons.constants.cluster.ClusterFaultMechanism;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author xuliangliang
 * @Classname BlogServiceImpl
 * @Description 博客实现类
 * @Date 2019/10/30 22:23
 * @blame Java Team
 */
@Service(version = "1.0", timeout = 10000, cluster = ClusterFaultMechanism.FAIL_OVER)
public class BlogServiceImpl implements IBlogService {

    @Autowired BlogMapper blogMapper;

    @Autowired BlogContentDao blogContentDao;

    @Autowired BlogCategoryMapper blogCategoryMapper;

    @Autowired BlogLabelDao blogLabelDao;

    /**
     * 查看自己的博客列表
     *
     * @param ownBlogListRequest
     * @return
     */
    @Override
    @DB(DBGroup.SLAVE)
    public OwnBlogListResponse ownBlogList(OwnBlogListRequest ownBlogListRequest) {
        OwnBlogListResponse response = new OwnBlogListResponse();

        Long userId = ownBlogListRequest.getUserId();
        Long categoryId = ownBlogListRequest.getCategoryId();
        Integer pageNo = ownBlogListRequest.getPageNo();
        Integer pageSize = ownBlogListRequest.getPageSize();

        RowBounds rowBounds = new RowBounds(pageSize * (pageNo - 1), pageSize);
        Example example = new Example(Blog.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("pkUserId", userId);
        if (categoryId != null && categoryId >= 0) {
            criteria.andEqualTo("categoryId", categoryId);
        }
        criteria.andEqualTo("status", BlogStatusEnum.NORMAL.status());

        long matchedBlogCount = blogMapper.selectCountByExample(example);
        response.setTotal(matchedBlogCount);
        if (matchedBlogCount > 0) {
            example.setOrderByClause("id desc");

            List<Blog> blogs = blogMapper.selectByExampleAndRowBounds(example, rowBounds);
            response.setBlogs(
                    blogs.parallelStream().map(blog -> {
                        // 分类
                        String category = Optional.ofNullable(blog.getCategoryId())
                                .map(cId -> {
                                    if (cId > 0) {
                                        return Optional.ofNullable(blogCategoryMapper.selectByPrimaryKey(cId))
                                                .map(BlogCategory::getCategory)
                                                .orElse(StringUtils.EMPTY);
                                    }
                                    return StringUtils.EMPTY;
                                }).orElse(StringUtils.EMPTY);

                        // 标签
                        List<String> labels = blogLabelDao.listBlogLabels(blog.getId());

                        return BlogItem.Builder.aBlogItem()
                                .withBlogId(BlogIdEncrypt.encrypt(blog.getId()))
                                .withTitle(blog.getTitle())
                                .withSummary(blog.getSummary())
                                .withCoverImg(blog.getCoverImg())
                                .withCategory(category)
                                .withLabels(labels)
                                .withVersion(blog.getVersion())
                                .withGmtCreate(blog.getGmtCreate())
                                .withGmtModified(blog.getGmtModified())
                                .build();
                    }).collect(Collectors.toList())
            );
        } else {
            response.setBlogs(Collections.EMPTY_LIST);
        }

        response.setCode(SysRetCodeConstants.SUCCESS.getCode());
        response.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
        return response;
    }

    /**
     * 获取博客 markdown 内容
     *
     * @param markdownRequest
     * @return
     */
    @Override
    public BlogMarkdownResponse markdown(BlogMarkdownRequest markdownRequest) {
        BlogMarkdownResponse markdownResponse = new BlogMarkdownResponse();


        BlogIdEncrypt.BlogKey blogKey = BlogIdEncrypt.decrypt(markdownRequest.getBlogId());
        Blog blog = Optional.ofNullable(blogKey).map(BlogIdEncrypt.BlogKey::getBlogId)
                .map(blogMapper::selectByPrimaryKey).orElse(null);

        boolean blogExists = blog != null && Objects.equals(blog.getStatus(), BlogStatusEnum.NORMAL.status());

        if (blogExists) {
            final int VERSION = blogKey.getVersion() > 0 ? blogKey.getVersion() : blog.getVersion();
            String markdown = blogContentDao.markdown(blog.getId(), VERSION);

            BlogItemWithMarkdown blogItemWithMarkdown = new BlogItemWithMarkdown();
            blogItemWithMarkdown.setBlogId(BlogIdEncrypt.encrypt(blog.getId()));
            blogItemWithMarkdown.setTitle(blog.getTitle());
            blogItemWithMarkdown.setVersion(VERSION);
            blogItemWithMarkdown.setMarkdown(markdown);
            blogItemWithMarkdown.setGmtCreate(blog.getGmtCreate());
            blogItemWithMarkdown.setGmtModified(blog.getGmtModified());

            markdownResponse.setCode(SysRetCodeConstants.SUCCESS.getCode());
            markdownResponse.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
            markdownResponse.setBlog(blogItemWithMarkdown);
        } else {
            markdownResponse.setCode(SysRetCodeConstants.BLOG_NOT_EXISTS.getCode());
            markdownResponse.setMsg(SysRetCodeConstants.BLOG_NOT_EXISTS.getMsg());
        }

        return markdownResponse;
    }
}
