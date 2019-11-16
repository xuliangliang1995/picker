package com.grasswort.picker.blog.dao.persistence;

import com.grasswort.picker.blog.dao.entity.BlogTrigger;
import com.grasswort.picker.commons.tkmapper.TkMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface BlogTriggerMapper extends TkMapper<BlogTrigger> {

    @Select("select * from pk_blog_trigger where blog_id = #{blogId}")
    BlogTrigger selectOneByBlogId(@Param("blogId") Long blogId);

}