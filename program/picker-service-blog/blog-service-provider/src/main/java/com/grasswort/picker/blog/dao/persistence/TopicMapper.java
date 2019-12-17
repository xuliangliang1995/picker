package com.grasswort.picker.blog.dao.persistence;

import com.grasswort.picker.blog.dao.entity.Topic;
import com.grasswort.picker.commons.tkmapper.TkMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface TopicMapper extends TkMapper<Topic> {

    @Select("select id from pk_topic where pk_user_id = #{authorId}")
    List<Long> listTopic(@Param("authorId") long authorId);
}