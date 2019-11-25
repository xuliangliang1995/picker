package com.grasswort.picker.blog.dao.persistence;

import com.grasswort.picker.blog.dao.entity.BlogBrowse;
import com.grasswort.picker.commons.tkmapper.TkMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface BlogBrowseMapper extends TkMapper<BlogBrowse> {

    @Select("select count(*) from pk_blog_browse where blog_id = #{blogId}")
    long getBrowseCount(@Param("blogId") long blogId);
}