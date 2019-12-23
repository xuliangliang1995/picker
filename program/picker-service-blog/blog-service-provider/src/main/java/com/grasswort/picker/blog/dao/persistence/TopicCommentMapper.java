package com.grasswort.picker.blog.dao.persistence;

import com.grasswort.picker.blog.dao.entity.TopicComment;
import com.grasswort.picker.commons.tkmapper.TkMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface TopicCommentMapper extends TkMapper<TopicComment> {

    @Select("select id from pk_topic_comment where topic_id = #{topicId} and user_id = #{userId}")
    Long selectIdByUserIdAndTopicId(@Param("userId") Long userId, @Param("topicId") Long topicId);
}