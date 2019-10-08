package com.grasswort.picker.user.service;

import com.grasswort.picker.commons.annotation.DB;
import com.grasswort.picker.commons.constants.TOrF;
import com.grasswort.picker.commons.constants.cluster.ClusterFaultMechanism;
import com.grasswort.picker.commons.constants.cluster.ClusterLoadBalance;
import com.grasswort.picker.user.IUserRegisterService;
import com.grasswort.picker.user.constants.DBGroup;
import com.grasswort.picker.user.constants.SysRetCodeConstants;
import com.grasswort.picker.user.dao.entity.User;
import com.grasswort.picker.user.dao.persistence.UserMapper;
import com.grasswort.picker.user.dto.UserRegisterRequest;
import com.grasswort.picker.user.dto.UserRegisterResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;

/**
 * @author xuliangliang
 * @Classname UserRegisterServiceImpl
 * @Description 用户注册
 * @Date 2019/9/22 9:25
 * @blame Java Team
 */
@Slf4j
@Service(
        timeout = 1000,
        loadbalance = ClusterLoadBalance.LEAST_ACTIVE,
        cluster = ClusterFaultMechanism.FAIL_FAST,
        validation = TOrF.FALSE,
        version = "1.0"
)
public class UserRegisterServiceImpl implements IUserRegisterService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    UserActivateServiceImpl userActivateServiceImpl;

    @DB(DBGroup.MASTER)
    @Override
    public UserRegisterResponse register(UserRegisterRequest request) {
        log.info("\n用户注册：{}", request.getUsername());
        UserRegisterResponse response = new UserRegisterResponse();

        Example example = new Example(User.class);
        example.createCriteria().andEqualTo("username", request.getUsername());
        User user = userMapper.selectOneByExample(example);
        if (user != null) {
            response.setCode(SysRetCodeConstants.USERNAME_ALREADY_EXISTS.getCode());
            response.setMsg(SysRetCodeConstants.USERNAME_ALREADY_EXISTS.getMsg());
            log.info("\n用户名已存在：{}", request.getUsername());
            return response;
        }

        Date now = DateTime.now().toDate();
        user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(DigestUtils.md5DigestAsHex(request.getPassword().getBytes()));
        user.setEmail(request.getEmail());
        user.setName(request.getUsername());
        user.setSex((byte) 0);
        user.setActivated(false);
        user.setVersion(1);
        user.setGmtCreate(now);
        user.setGmtModified(now);
        int result = 0;
        try {
            result = userMapper.insertUseGeneratedKeys(user);
        } catch (Exception e) {
            log.info("\n操作数据库异常：{}", e.getMessage());
        }

        if (result <= 0) {
            response.setCode(SysRetCodeConstants.DB_EXCEPTION.getCode());
            response.setMsg(SysRetCodeConstants.DB_EXCEPTION.getMsg());
            log.info("\n注册失败：{}", request.getUsername());
            return response;
        }

        response.setCode(SysRetCodeConstants.SUCCESS.getCode());
        response.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
        log.info("\n注册成功：{}", request.getUsername());
        // 发送激活邮件
        userActivateServiceImpl.sendActivateEmail(user.getId());
        return response;
    }


}
