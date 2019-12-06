package com.grasswort.picker.user.dao.persistence;

import com.grasswort.picker.commons.tkmapper.TkMapper;
import com.grasswort.picker.user.dao.entity.UserSubscribeAuthor;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserSubscribeAuthorMapper extends TkMapper<UserSubscribeAuthor> {

    @Select("select count(*) from pk_user_subscribe_author where pk_user_id = #{pkUserId}")
    Long subscribeCount(@Param("pkUserId") Long pkUserId);

    @Select("select count(*) from pk_user_subscribe_author where author_id = #{pkUserId}")
    Long fansCount(@Param("pkUserId") Long pkUserId);

    @Select("select if(count(*) > 0, 1, 0) from pk_user_subscribe_author where pk_user_id = #{pkUserId} and author_id = #{authorId}")
    Boolean isSubscribe(@Param("pkUserId") Long pkUserId, @Param("authorId") Long authorId);

    @Select("select pk_user_id from pk_user_subscribe_author where author_id = #{authorId} order by id desc")
    List<Long> followerIdList(@Param("authorId") Long authorId);

    @Select("select author_id from pk_user_subscribe_author where pk_user_id = #{pkUserId} order by id desc")
    List<Long> followingIdList(@Param("pkUserId") Long pkUserId);

}