package com.grasswort.picker.user.bootstrap;

import com.grasswort.picker.commons.config.DBLocalHolder;
import com.grasswort.picker.user.constants.DBGroup;
import com.grasswort.picker.user.dao.entity.User;
import com.grasswort.picker.user.dao.persistence.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.spring.annotation.MapperScan;

import java.util.List;

/**
 * @author xuliangliang
 * @Classname UserServiceProviderApplication
 * @Description 启动类
 * @Date 2019/9/22 9:03
 * @blame Java Team
 */
@SpringBootApplication
@ComponentScan(basePackages = {
        "com.grasswort.picker.user",
        "com.grasswort.picker.commons.config"
})
@Slf4j
@MapperScan("com.grasswort.picker.user.dao.persistence")
public class UserServiceProviderApplication {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(UserServiceProviderApplication.class, args);
        DBLocalHolder.selectDBGroup(DBGroup.SLAVE);
        UserMapper userMapper = ctx.getBean(UserMapper.class);
        Example example = new Example(User.class);
        List<User> users = userMapper.selectByExample(example);
        log.info("注册用户数为：{}", users.size());
    }
}
