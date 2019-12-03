package com.grasswort.picker.blog.dao.persistence.ext;

import com.grasswort.picker.blog.dao.entity.Blog;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

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
}
