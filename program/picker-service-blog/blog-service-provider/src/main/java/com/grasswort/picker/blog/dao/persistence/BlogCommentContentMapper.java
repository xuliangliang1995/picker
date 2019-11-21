package com.grasswort.picker.blog.dao.persistence;

import com.grasswort.picker.blog.dao.entity.BlogCommentContent;
import com.grasswort.picker.commons.tkmapper.TkMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface BlogCommentContentMapper extends TkMapper<BlogCommentContent> {

    @Select("select content from pk_blog_comment_content where comment_id = #{commentId}")
    String getContent(@Param("commentId") Long commentId);
}