package com.grasswort.picker.blog.service.elastic;

import com.grasswort.picker.blog.constant.DBGroup;
import com.grasswort.picker.blog.dao.entity.Blog;
import com.grasswort.picker.blog.dto.blog.BlogItemWithAuthor;
import com.grasswort.picker.blog.dto.blog.InteractionData;
import com.grasswort.picker.blog.elastic.entity.BlogDoc;
import com.grasswort.picker.blog.service.elastic.converter.Blog2DocConverter;
import com.grasswort.picker.blog.service.elastic.converter.BlogDocMapStructConverter;
import com.grasswort.picker.blog.util.BlogIdEncrypt;
import com.grasswort.picker.commons.annotation.DB;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author xuliangliang
 * @Classname BlogDocConverter
 * @Description 博客转换
 * @Date 2019/12/2 15:57
 * @blame Java Team
 */
@Service
public class BlogDocConverter {
    @Resource Blog2DocConverter blog2DocConverter;

    @Resource BlogDocMapStructConverter blogDocMapStructConverter;

    /**
     * blog2BlogDoc
     * @param blog
     * @return
     */
    public BlogDoc blog2BlogDoc(Blog blog) {
        return blog2DocConverter.blog2BlogDoc(blog);
    }

    /**
     * blogDoc2BlogItemWithAuthor
     * @param blogDoc
     * @return
     */
    @DB(DBGroup.SLAVE)
    public BlogItemWithAuthor blogDoc2BlogItemWithAuthor(BlogDoc blogDoc) {
        Long blogId = blogDoc.getBlogId();
        BlogItemWithAuthor blogItemWithAuthor = blogDocMapStructConverter.doc2BlogItemWithAuthor(blogDoc);
        InteractionData interactionData = InteractionData.Builder.anInteractionData()
                .withLike(blogDoc.getLike())
                .withFavorite(blogDoc.getFavorite())
                .withBrowse(blogDoc.getBrowse())
                .build();
        blogItemWithAuthor.setBlogId(BlogIdEncrypt.encrypt(blogId));
        blogItemWithAuthor.setInteraction(interactionData);
        return blogItemWithAuthor;
    }
}
