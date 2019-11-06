package com.grasswort.picker.blog.dao.persistence.ext;

import com.grasswort.picker.blog.dao.entity.BlogCategory;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author xuliangliang
 * @Classname BlogCategoryDao
 * @Description 博客分类（自定义sql)
 * @Date 2019/10/21 9:45
 * @blame Java Team
 */
public interface BlogCategoryDao extends Mapper<BlogCategory> {

    @Select("select pk_user_id from pk_blog_category where id = #{categoryId}")
    Long selectUserIdByPrimaryKey(@Param("categoryId") Long categoryId);

    @Select("select id from pk_blog_category where pk_user_id = #{pkUserId} and parent_id = #{parentId} and category = #{category}")
    Long selectIdByPkUserIdAndCategory(@Param("pkUserId") Long pkUserId, @Param("parentId") Long parentId, @Param("category") String category);

}
