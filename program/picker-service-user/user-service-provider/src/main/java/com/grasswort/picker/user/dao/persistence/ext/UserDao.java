package com.grasswort.picker.user.dao.persistence.ext;

import com.grasswort.picker.user.dao.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author xuliangliang
 * @Classname UserDao
 * @Description UserMapper 扩展
 * @Date 2019/10/9 14:22
 * @blame Java Team
 */
public interface UserDao extends Mapper<User> {

    @Select("select version from pk_user where id = #{userId}")
    int selectVersionByUserId(Long userId);

    @Update("update pk_user set password = #{password}, version = #{newVersion} where id = #{userId} and version = #{oldVersion}")
    int updatePassword(@Param("userId") Long userId, @Param("password") String password, @Param("oldVersion") int oldVersion, @Param("newVersion") int newVersion);
}
