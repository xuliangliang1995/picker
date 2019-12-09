package com.grasswort.picker.blog.service;

import com.grasswort.picker.blog.IBlogPoolService;
import com.grasswort.picker.blog.constant.DBGroup;
import com.grasswort.picker.blog.constant.SysRetCodeConstants;
import com.grasswort.picker.blog.dao.persistence.*;
import com.grasswort.picker.blog.dao.persistence.ext.BlogLabelDao;
import com.grasswort.picker.blog.dto.BlogPoolQueryRequest;
import com.grasswort.picker.blog.dto.BlogPoolQueryResponse;
import com.grasswort.picker.blog.elastic.entity.BlogDoc;
import com.grasswort.picker.blog.service.elastic.BlogDocConverter;
import com.grasswort.picker.blog.service.elastic.BlogDocInitService;
import com.grasswort.picker.blog.service.elastic.BlogSearchService;
import com.grasswort.picker.blog.service.elastic.dto.SearchParams;
import com.grasswort.picker.blog.service.hotword.SearchHotWordService;
import com.grasswort.picker.commons.annotation.DB;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import javax.annotation.Resource;
import java.util.stream.Collectors;

/**
 * @author xuliangliang
 * @Classname BlogPoolServiceImpl
 * @Description
 * @Date 2019/11/20 15:58
 * @blame Java Team
 */
@Service(version = "1.0", timeout = 10000)
public class BlogPoolServiceImpl implements IBlogPoolService {

    @Autowired BlogMapper blogMapper;

    @Autowired BlogLabelMapper blogLabelMapper;

    @Autowired BlogLabelDao blogLabelDao;

    @Autowired BlogLikeMapper blogLikeMapper;

    @Autowired BlogFavoriteMapper blogFavoriteMapper;

    @Autowired BlogBrowseMapper blogBrowseMapper;

    @Autowired BlogDocConverter blogDocConverter;

    @Autowired BlogSearchService blogSearchService;

    @Autowired SearchHotWordService searchHotWordService;

    @Autowired BlogDocInitService blogDocInitService;

    /**
     * 获取博客列表
     *
     * @param queryRequest
     * @return
     */
    @Override
    @DB(DBGroup.SLAVE)
    public BlogPoolQueryResponse blogPool(BlogPoolQueryRequest queryRequest) {
        BlogPoolQueryResponse response = new BlogPoolQueryResponse();

        String keyword = queryRequest.getKeyword();
        Long authorId = queryRequest.getAuthorId();
        Integer pageNo = queryRequest.getPageNo();
        Integer pageSize = queryRequest.getPageSize();

        // 统计搜索热词
        searchHotWordService.staticsSearchHotWord(keyword);

        // 根据关键词搜索
        SearchParams searchParams = SearchParams.builder()
                .keyword(keyword)
                .authorId(authorId)
                .build();
        Page<BlogDoc> blogDocs = blogSearchService.search(searchParams, pageNo, pageSize);
        response.setBlogs(
                blogDocs.getContent().stream()
                        .map(doc -> blogDocConverter.blogDoc2BlogItemWithAuthor(doc))
                        .collect(Collectors.toList())
        );
        response.setTotal(blogDocs.getTotalElements());

        response.setCode(SysRetCodeConstants.SUCCESS.getCode());
        response.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
        return response;
    }

    /**
     * 初始化
     */
    @Override
    @DB(DBGroup.SLAVE)
    public void init() {
        blogDocInitService.init();
    }


    /// 从数据库中查询博客，已改成从 elasticsearch 中查找。暂时留着，以后再删
    /*RowBounds rowBounds = new RowBounds(pageSize * (pageNo - 1), pageSize);

    Example example = new Example(Blog.class);
            example.createCriteria().andEqualTo("status", BlogStatusEnum.NORMAL.status());
            example.setOrderByClause("id desc");
    long total = blogMapper.selectCountByExample(example);
    List<BlogItemWithAuthor> blogs = blogMapper.selectByExampleAndRowBounds(example, rowBounds).parallelStream().map(blog -> {
        // 标签
        List<String> labels = blogLabelDao.listBlogLabels(blog.getId());

        UserBaseInfoResponse baseInfoResponse = iUserBaseInfoService.userBaseInfo(UserBaseInfoRequest.Builder.anUserBaseInfoRequest().withUserId(blog.getPkUserId()).build());
        String author = Optional.ofNullable(baseInfoResponse).filter(UserBaseInfoResponse::isSuccess).map(UserBaseInfoResponse::getName).orElse("");
        String avatar = Optional.ofNullable(baseInfoResponse).filter(UserBaseInfoResponse::isSuccess).map(UserBaseInfoResponse::getAvatar).orElse("");
        BlogItemWithAuthor blogWithAuthor = BlogItemWithAuthor.Builder.aBlogItemWithAuthor()
                .withPickerId(PickerIdEncrypt.encrypt(blog.getPkUserId()))
                .withBlogId(BlogIdEncrypt.encrypt(blog.getId()))
                .withTitle(blog.getTitle())
                .withSummary(blog.getSummary())
                .withCoverImg(blog.getCoverImg())
                .withLabels(labels)
                .withVersion(blog.getVersion())
                .withGmtCreate(blog.getGmtCreate())
                .withGmtModified(blog.getGmtModified())
                .withAuthor(author)
                .withAuthorAvatar(avatar)
                .build();

        InteractionData interactionData = InteractionData.Builder.anInteractionData()
                .withLike(blogLikeMapper.getLikeCount(blog.getId()))
                .withFavorite(blogFavoriteMapper.getBlogFavoriteCount(blog.getId()))
                .withBrowse(blogBrowseMapper.getBrowseCount(blog.getId()))
                .build();
        blogWithAuthor.setInteraction(interactionData);

        return blogWithAuthor;
    }).collect(Collectors.toList());
            response.setBlogs(blogs);
            response.setTotal(total);*/
}
