package com.grasswort.picker.blog.dao.persistence;

import com.grasswort.picker.blog.dao.entity.TopicFavorite;
import com.grasswort.picker.commons.tkmapper.TkMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface TopicFavoriteMapper extends TkMapper<TopicFavorite> {

    @Select("select id from pk_topic_favorite where user_id = #{userId} and topic_id = #{topicId}")
    Long selectIdByUserIdAndTopicId(@Param("userId") Long userId, @Param("topicId") Long topicId);

    @Select("select topic_id from pk_topic_favorite where user_id = #{userId} order by id desc")
    List<Long> listTopicFavorite(@Param("userId") Long userId);

}