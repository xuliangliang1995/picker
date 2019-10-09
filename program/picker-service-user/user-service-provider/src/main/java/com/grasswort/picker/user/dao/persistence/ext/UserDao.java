package com.grasswort.picker.user.dao.persistence.ext;

import com.grasswort.picker.user.dao.entity.User;
import org.apache.ibatis.annotations.Select;
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
}
