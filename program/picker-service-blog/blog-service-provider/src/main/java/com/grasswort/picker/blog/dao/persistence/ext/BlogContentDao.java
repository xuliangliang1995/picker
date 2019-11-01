package com.grasswort.picker.blog.dao.persistence.ext;

import com.grasswort.picker.blog.dao.entity.BlogContent;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author xuliangliang
 * @Classname BlogContentDao
 * @Description 博客内容查询
 * @Date 2019/10/31 18:32
 * @blame Java Team
 */
public interface BlogContentDao extends Mapper<BlogContent> {

    @Select("select markdown from pk_blog_content where blog_id = #{blogId} and blog_version = #{version}")
    String markdown(@Param("blogId") Long blogId, @Param("version") Integer version);

    @Select("select html from pk_blog_content where blog_id = #{blogId} and blog_version = #{version}")
    String html(@Param("blogId") Long blogId, @Param("version") Integer version);
}
