package com.grasswort.picker.blog.dao.persistence.ext;

import com.grasswort.picker.blog.dao.entity.Blog;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author xuliangliang
 * @Classname BlogDao
 * @Description 博客
 * @Date 2019/11/7 16:02
 * @blame Java Team
 */
public interface BlogDao extends Mapper<Blog> {

    @Select("select count(*) from pk_blog where category_id = #{categoryId} and status = #{status}")
    Long selectCountByCategoryIdAndStatus(@Param("categoryId") Long categoryId, @Param("status") Integer status);

    @Select("select count(*) from pk_blog where pk_user_id = #{pkUserId} and status = 0")
    Long getBlogCount(@Param("pkUserId") Long pkUserId);

    @Select("select pk_user_id from pk_blog where id = #{blogId}")
    Long getPkUserId(@Param("blogId") Long blogId);

    @Select("select id from pk_blog where pk_user_id = #{authorId} order by id desc")
    List<Long> listBlogIdByAuthorId(@Param("authorId") Long authorId);

    @Select("select if(count(*) > 0, 1, 0)  from pk_blog where id = #{blogId} and status = 0")
    Boolean existsAndNormal(@Param("blogId") Long blogId);


}
