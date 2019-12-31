package com.grasswort.picker.user.dao.persistence;

import com.grasswort.picker.commons.tkmapper.TkMapper;
import com.grasswort.picker.user.dao.entity.UserProfile;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface UserProfileMapper extends TkMapper<UserProfile> {

    @Select("select id from pk_user_profile where pk_user_id = #{pkUserId}")
    Long selectIdByPkUserId(@Param("pkUserId") Long pkUserId);
}