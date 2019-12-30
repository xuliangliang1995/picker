package com.grasswort.picker.blog.dao.persistence;

import com.grasswort.picker.blog.dao.entity.BlogFavorite;
import com.grasswort.picker.commons.tkmapper.TkMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface BlogFavoriteMapper extends TkMapper<BlogFavorite> {

    @Select("select count(*) from pk_blog_favorite where blog_id = #{blogId}")
    long getBlogFavoriteCount(@Param("blogId") long blogId);

    @Select("select blog_id from pk_blog_favorite where user_id = #{userId} order by id desc")
    List<Long> listBlogIdFavorite(@Param("userId") long userId);
}