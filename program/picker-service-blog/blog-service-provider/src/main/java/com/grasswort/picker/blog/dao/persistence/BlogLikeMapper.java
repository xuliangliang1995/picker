package com.grasswort.picker.blog.dao.persistence;

import com.grasswort.picker.blog.dao.entity.BlogLike;
import com.grasswort.picker.commons.tkmapper.TkMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface BlogLikeMapper extends TkMapper<BlogLike> {

    @Select("select count(*) from pk_blog_like where blog_id = #{blogId}")
    long getLikeCount(@Param("blogId") long blogId);
}