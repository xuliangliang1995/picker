package com.grasswort.picker.user.service;

import com.grasswort.picker.commons.constants.TOrF;
import com.grasswort.picker.commons.constants.cluster.ClusterFaultMechanism;
import com.grasswort.picker.commons.constants.cluster.ClusterLoadBalance;
import com.grasswort.picker.user.IUserRegisterService;
import com.grasswort.picker.user.constants.SysRetCodeConstants;
import com.grasswort.picker.user.dao.persistence.UserMapper;
import com.grasswort.picker.user.dto.UserRegisterRequest;
import com.grasswort.picker.user.dto.UserRegisterResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

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
        validation = TOrF.TRUE,
        version = "1.0"
)
public class UserRegisterServiceImpl implements IUserRegisterService {
    @Autowired
    UserMapper userMapper;

    @Override
    public UserRegisterResponse register(UserRegisterRequest request) {
        log.info("\n用户注册：{}", request.getUsername());
        // System.out.println(1 / 0); 故意抛异常测试 Mock 是否生效
        UserRegisterResponse response = new UserRegisterResponse();
        response.setCode(SysRetCodeConstants.SUCCESS.getCode());
        response.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
        log.info("\n注册成功：{}", request.getUsername());
        return response;
    }

}
