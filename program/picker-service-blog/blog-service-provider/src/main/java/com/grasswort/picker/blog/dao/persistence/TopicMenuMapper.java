package com.grasswort.picker.blog.dao.persistence;

import com.grasswort.picker.blog.dao.entity.TopicMenu;
import com.grasswort.picker.commons.tkmapper.TkMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface TopicMenuMapper extends TkMapper<TopicMenu> {

    @Select("select max(weight) from pk_topic_menu where topic_id = #{topicId} and parent_menu_id = #{parentMenuId}")
    Integer equativeMaxWeight(@Param("topicId") Long topicId, @Param("parentMenuId") Long parentMenuId);

    @Select("select if(count(*) > 0, 1, 0) from pk_topic_menu where parent_menu_id = #{parentMenuId}")
    Boolean hasChildren(@Param("parentMenuId") Long parentMenuId);
}