package com.grasswort.picker.blog.service.elastic.converter;

import com.grasswort.picker.blog.dao.entity.Blog;
import com.grasswort.picker.blog.dao.entity.BlogContent;
import com.grasswort.picker.blog.dao.entity.BlogLabel;
import com.grasswort.picker.blog.dao.persistence.BlogContentMapper;
import com.grasswort.picker.blog.dao.persistence.BlogLabelMapper;
import com.grasswort.picker.blog.dao.persistence.ext.BlogLabelDao;
import com.grasswort.picker.blog.dto.blog.BlogItemWithAuthor;
import com.grasswort.picker.blog.elastic.entity.BlogDoc;
import com.grasswort.picker.blog.util.BlogIdEncrypt;
import com.grasswort.picker.user.IUserBaseInfoService;
import com.grasswort.picker.user.dto.UserBaseInfoRequest;
import com.grasswort.picker.user.dto.UserBaseInfoResponse;
import com.grasswort.picker.user.util.PickerIdEncrypt;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
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

    @Reference(version = "1.0", timeout = 10000)
    IUserBaseInfoService iUserBaseInfoService;

    /**
     * blog2BlogDoc
     * @param blog
     * @return
     */
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
                .build();

        return blogDoc;
    }
}
