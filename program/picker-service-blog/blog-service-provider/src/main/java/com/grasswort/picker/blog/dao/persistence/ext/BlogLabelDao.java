package com.grasswort.picker.blog.dao.persistence.ext;

import com.grasswort.picker.blog.dao.entity.BlogLabel;
import com.grasswort.picker.commons.tkmapper.TkMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

/**
 * @author xuliangliang
 * @Classname BlogLabelDao
 * @Description 博客标签
 * @Date 2019/11/1 23:17
 * @blame Java Team
 */
public interface BlogLabelDao extends TkMapper<BlogLabel> {

    @Select("select label from pk_blog_label where blog_id = #{blogId}")
    List<String> listBlogLabels(@Param("blogId") Long blogId);

    @Delete("delete from pk_blog_label where blog_id = #{blogId}")
    int deleteLabelByBlogId(@Param("blogId") Long blogId);

    @Select("select label from pk_blog_label")
    Set<String> getAllLabels();
}
