package com.grasswort.picker.user.dao.persistence;

import com.grasswort.picker.commons.tkmapper.TkMapper;
import com.grasswort.picker.user.dao.entity.UserSubscribeAuthor;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface UserSubscribeAuthorMapper extends TkMapper<UserSubscribeAuthor> {

    @Select("select count(*) from pk_user_subscribe_author where pk_user_id = #{pkUserId}")
    Long subscribeCount(@Param("pkUserId") Long pkUserId);

    @Select("select count(*) from pk_user_subscribe_author where author_id = #{pkUserId}")
    Long fansCount(@Param("pkUserId") Long pkUserId);
}