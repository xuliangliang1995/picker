package com.grasswort.picker.blog.service.elastic.converter;

import com.grasswort.picker.blog.constant.DBGroup;
import com.grasswort.picker.blog.dao.entity.Blog;
import com.grasswort.picker.blog.dao.entity.BlogContent;
import com.grasswort.picker.blog.dao.persistence.*;
import com.grasswort.picker.blog.dao.persistence.ext.BlogLabelDao;
import com.grasswort.picker.blog.elastic.entity.BlogDoc;
import com.grasswort.picker.blog.elastic.repository.BlogDocRepository;
import com.grasswort.picker.blog.service.elastic.BlogDocConverter;
import com.grasswort.picker.blog.service.heat.HackerNewsHeat;
import com.grasswort.picker.commons.annotation.DB;
import com.grasswort.picker.user.IUserBaseInfoService;
import com.grasswort.picker.user.dto.UserBaseInfoRequest;
import com.grasswort.picker.user.dto.UserBaseInfoResponse;
import com.grasswort.picker.user.util.PickerIdEncrypt;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

/**
 * @author xuliangliang
 * @Classname BlogDocConverter
 * @Description 博客对象转 BlogDoc
 * @Date 2019/11/30 16:15
 * @blame Java Team
 */
@Service
public class Blog2DocConverter {

    @Resource BlogContentMapper blogContentMapper;

    @Resource BlogLabelDao blogLabelDao;

    @Resource BlogLikeMapper blogLikeMapper;

    @Resource BlogFavoriteMapper blogFavoriteMapper;

    @Resource BlogBrowseMapper blogBrowseMapper;

    @Reference(version = "1.0", timeout = 10000)
    IUserBaseInfoService iUserBaseInfoService;

    @Resource
    BlogMapper blogMapper;

    @Resource
    BlogDocRepository blogDocRepository;

    @Resource
    BlogDocConverter blogDocConverter;

    @PostConstruct
    public void init() {
        blogMapper.selectAll().forEach(blog -> blogDocRepository.save(blogDocConverter.blog2BlogDoc(blog)));
    }

    /**
     * blog2BlogDoc
     * @param blog
     * @return
     */
    @DB(DBGroup.SLAVE)
    public BlogDoc blog2BlogDoc(Blog blog) {
        List<String> labels = blogLabelDao.listBlogLabels(blog.getId());
        Example example = new Example(BlogContent.class);
        example.createCriteria().andEqualTo("blogId", blog.getId())
                .andEqualTo("blogVersion", blog.getVersion());
        BlogContent content = blogContentMapper.selectOneByExample(example);

        String author = null, authorAvatar = null;
        UserBaseInfoResponse baseInfoResponse = iUserBaseInfoService.userBaseInfo(
                UserBaseInfoRequest.Builder.anUserBaseInfoRequest()
                .withUserId(blog.getPkUserId())
                .build()
        );
        if (baseInfoResponse.isSuccess()) {
            author = baseInfoResponse.getName();
            authorAvatar = baseInfoResponse.getAvatar();
        }

        Long like = blogLikeMapper.getLikeCount(blog.getId());
        Long favorite = blogFavoriteMapper.getBlogFavoriteCount(blog.getId());
        Long browse = blogBrowseMapper.getBrowseCount(blog.getId());
        Date gmtCreate = blog.getGmtCreate();
        Double heat = HackerNewsHeat.calculate((like + favorite) * 5, Duration.between(gmtCreate.toInstant(), Instant.now()).get(ChronoUnit.MINUTES) / (24 * 60));

        BlogDoc blogDoc = BlogDoc.Builder.aBlogDoc()
                .withBlogId(blog.getId())
                .withPickerId(PickerIdEncrypt.encrypt(blog.getPkUserId()))
                .withTitle(blog.getTitle())
                .withSummary(blog.getSummary())
                .withCoverImg(blog.getCoverImg())
                .withVersion(blog.getVersion())
                .withGmtCreate(blog.getGmtCreate())
                .withGmtModified(content.getGmtModified())
                .withLabels(labels)
                .withMarkdown(content.getMarkdown())
                .withAuthorId(blog.getPkUserId())
                .withAuthor(author)
                .withAuthorAvatar(authorAvatar)
                .withStatus(blog.getStatus())
                .withLike(like)
                .withFavorite(favorite)
                .withBrowse(browse)
                .withHeat(heat)
                .build();

        return blogDoc;
    }
}
