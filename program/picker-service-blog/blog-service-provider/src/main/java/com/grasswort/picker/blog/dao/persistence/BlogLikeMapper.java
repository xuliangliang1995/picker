package com.grasswort.picker.blog.dao.persistence;

import com.grasswort.picker.blog.dao.entity.BlogLike;
import com.grasswort.picker.commons.tkmapper.TkMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface BlogLikeMapper extends TkMapper<BlogLike> {

    @Select("select count(*) from pk_blog_like where blog_id = #{blogId}")
    long getLikeCount(@Param("blogId") long blogId);

    @Select("select count(bl.id) from pk_blog as blog\n" +
            "left join pk_blog_like as bl on bl.blog_id = blog.id\n" +
            "where blog.pk_user_id = #{pkUserId}")
    long getUserLikedCount(@Param("pkUserId") Long pkUserId);
}