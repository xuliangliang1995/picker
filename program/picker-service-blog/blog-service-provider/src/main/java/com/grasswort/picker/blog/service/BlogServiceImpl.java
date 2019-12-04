package com.grasswort.picker.blog.service;

import com.grasswort.picker.blog.IBlogService;
import com.grasswort.picker.blog.constant.BlogCurveStatusEnum;
import com.grasswort.picker.blog.constant.BlogStatusEnum;
import com.grasswort.picker.blog.constant.DBGroup;
import com.grasswort.picker.blog.constant.SysRetCodeConstants;
import com.grasswort.picker.blog.dao.entity.Blog;
import com.grasswort.picker.blog.dao.entity.BlogCategory;
import com.grasswort.picker.blog.dao.entity.BlogContent;
import com.grasswort.picker.blog.dao.entity.BlogTrigger;
import com.grasswort.picker.blog.dao.persistence.*;
import com.grasswort.picker.blog.dao.persistence.ext.BlogLabelDao;
import com.grasswort.picker.blog.dto.*;
import com.grasswort.picker.blog.dto.blog.BlogItem;
import com.grasswort.picker.blog.dto.blog.BlogItemWithMarkdown;
import com.grasswort.picker.blog.dto.blog.InteractionData;
import com.grasswort.picker.blog.util.BlogHtml;
import com.grasswort.picker.blog.util.BlogIdEncrypt;
import com.grasswort.picker.commons.annotation.DB;
import com.grasswort.picker.commons.constants.cluster.ClusterFaultMechanism;
import com.grasswort.picker.user.util.PickerIdEncrypt;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.*;
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

    @Autowired BlogCategoryMapper blogCategoryMapper;

    @Autowired BlogLabelDao blogLabelDao;

    @Autowired BlogContentMapper blogContentMapper;

    @Autowired BlogTriggerMapper blogTriggerMapper;

    @Autowired BlogLikeMapper blogLikeMapper;

    @Autowired BlogFavoriteMapper blogFavoriteMapper;

    @Autowired BlogBrowseMapper blogBrowseMapper;

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
                        // 博客推送状态
                        Example ex = new Example(BlogTrigger.class);
                        ex.createCriteria().andEqualTo("blogId", blog.getId());
                        BlogTrigger trigger = blogTriggerMapper.selectOneByExample(ex);

                        BlogCurveStatusEnum curveStatusEnum = Optional.ofNullable(trigger)
                                .map(t -> Arrays.stream(BlogCurveStatusEnum.values())
                                        .filter(curve -> Objects.equals(curve.status(), t.getStatus()))
                                        .findFirst().orElse(BlogCurveStatusEnum.STOP))
                                .orElse(BlogCurveStatusEnum.STOP);

                        BlogItem blogItem =  BlogItem.Builder.aBlogItem()
                                .withPickerId(PickerIdEncrypt.encrypt(blog.getPkUserId()))
                                .withBlogId(BlogIdEncrypt.encrypt(blog.getId()))
                                .withTitle(blog.getTitle())
                                .withSummary(blog.getSummary())
                                .withCoverImg(blog.getCoverImg())
                                .withCategory(category)
                                .withLabels(labels)
                                .withVersion(blog.getVersion())
                                .withTriggerStatus(curveStatusEnum.status())
                                .withGmtCreate(blog.getGmtCreate())
                                .withGmtModified(blog.getGmtModified())
                                .build();

                        InteractionData interactionData = InteractionData.Builder.anInteractionData()
                                .withLike(blogLikeMapper.getLikeCount(blog.getId()))
                                .withFavorite(blogFavoriteMapper.getBlogFavoriteCount(blog.getId()))
                                .withBrowse(blogBrowseMapper.getBrowseCount(blog.getId()))
                                .build();
                        blogItem.setInteraction(interactionData);

                        return blogItem;
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
    @DB(DBGroup.SLAVE)
    public BlogMarkdownResponse markdown(BlogMarkdownRequest markdownRequest) {
        BlogMarkdownResponse markdownResponse = new BlogMarkdownResponse();

        BlogIdEncrypt.BlogKey blogKey = BlogIdEncrypt.decrypt(markdownRequest.getBlogId());
        Blog blog = Optional.ofNullable(blogKey).map(BlogIdEncrypt.BlogKey::getBlogId)
                .map(blogMapper::selectByPrimaryKey).orElse(null);

        boolean blogExists = blog != null && Objects.equals(blog.getStatus(), BlogStatusEnum.NORMAL.status());

        if (blogExists) {
            final int VERSION = blogKey.getVersion() > 0 ? blogKey.getVersion() : blog.getVersion();
            Example example = new Example(BlogContent.class);
            example.createCriteria().andEqualTo("blogId", blog.getId())
                    .andEqualTo("blogVersion", VERSION);

            BlogContent content = blogContentMapper.selectOneByExample(example);

            BlogItemWithMarkdown blogItemWithMarkdown = BlogItemWithMarkdown.Builder.aBlogItemWithMarkdown()
                    .withPickerId(PickerIdEncrypt.encrypt(blog.getPkUserId()))
                    .withBlogId(markdownRequest.getBlogId())
                    .withTitle(blog.getTitle())
                    .withSummary(blog.getSummary())
                    .withCategoryId(blog.getCategoryId())
                    .withCoverImg(blog.getCoverImg())
                    .withVersion(VERSION)
                    .withMarkdown(content.getMarkdown())
                    .withLabels(blogLabelDao.listBlogLabels(blog.getId()))
                    .withGmtCreate(content.getGmtCreate())
                    .withGmtModified(content.getGmtModified())
                    .build();

            markdownResponse.setCode(SysRetCodeConstants.SUCCESS.getCode());
            markdownResponse.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
            markdownResponse.setBlog(blogItemWithMarkdown);
        } else {
            markdownResponse.setCode(SysRetCodeConstants.BLOG_NOT_EXISTS.getCode());
            markdownResponse.setMsg(SysRetCodeConstants.BLOG_NOT_EXISTS.getMsg());
        }

        return markdownResponse;
    }

    /**
     * 获取博客 HTML 内容
     *
     * @param htmlRequest
     * @return
     */
    @Override
    @DB(DBGroup.SLAVE)
    public BlogHtmlResponse html(BlogHtmlRequest htmlRequest) {
        BlogHtmlResponse htmlResponse = new BlogHtmlResponse();

        BlogIdEncrypt.BlogKey blogKey = BlogIdEncrypt.decrypt(htmlRequest.getBlogId());
        Blog blog = Optional.ofNullable(blogKey).map(BlogIdEncrypt.BlogKey::getBlogId)
                .map(blogMapper::selectByPrimaryKey).orElse(null);

        boolean blogExists = blog != null && Objects.equals(blog.getStatus(), BlogStatusEnum.NORMAL.status());

        if (blogExists) {
            final int VERSION = blogKey.getVersion() > 0 ? blogKey.getVersion() : blog.getVersion();
            Example example = new Example(BlogContent.class);
            example.createCriteria().andEqualTo("blogId", blog.getId())
                    .andEqualTo("blogVersion", VERSION);

            BlogContent content = blogContentMapper.selectOneByExample(example);

            String html = BlogHtml.Builder.aBlogHtml()
                    .withContent(content.getHtml())
                    .withTheme("github")
                    .build()
                    .toHtml();

            htmlResponse.setHtml(html);
        }
        if (StringUtils.isBlank(htmlResponse.getHtml())) {
            htmlResponse.setHtml(
                    BlogHtml.Builder.aBlogHtml()
                            .withContent("浏览资源不存在或已被删除")
                            .withTheme("github")
                            .build()
                            .toHtml()
            );
        }
        htmlResponse.setCode(SysRetCodeConstants.SUCCESS.getCode());
        htmlResponse.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
        return htmlResponse;
    }
}
