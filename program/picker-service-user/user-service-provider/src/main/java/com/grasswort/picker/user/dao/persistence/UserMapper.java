package com.grasswort.picker.user.dao.persistence;

import com.grasswort.picker.commons.tkmapper.TkMapper;
import com.grasswort.picker.user.dao.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserMapper extends TkMapper<User> {

    @Select("select private_mp_qrcode from pk_user where id = #{userId}")
    String getPrivateMpQrcode(@Param("userId") Long userId);

    @Select("select id from pk_user where is_activated = true")
    List<Long> listUserId();

}