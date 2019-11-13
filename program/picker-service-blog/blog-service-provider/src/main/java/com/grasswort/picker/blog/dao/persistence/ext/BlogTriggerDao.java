package com.grasswort.picker.blog.dao.persistence.ext;

import com.grasswort.picker.blog.dao.entity.BlogTrigger;
import com.grasswort.picker.commons.tkmapper.TkMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author xuliangliang
 * @Classname BlogTriggerDao
 * @Description BlogTrigger
 * @Date 2019/11/10 12:52
 * @blame Java Team
 */
public interface BlogTriggerDao extends TkMapper<BlogTrigger> {

    @Select("select * from pk_blog_trigger where blog_id = #{blogId}")
    BlogTrigger selectOneByBlogId(@Param("blogId") Long blogId);

    @Select("select * from pk_blog_trigger where datediff(trigger_time, now()) <= 0 and status = 0")
    List<BlogTrigger> listBlogTriggerToday();
}
