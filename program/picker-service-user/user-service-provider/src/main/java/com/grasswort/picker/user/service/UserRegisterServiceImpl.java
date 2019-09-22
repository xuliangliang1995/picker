package com.grasswort.picker.user.service;

import com.grasswort.picker.user.IUserRegisterService;
import com.grasswort.picker.user.constants.SysRetCodeConstants;
import com.grasswort.picker.user.dto.UserRegisterRequest;
import com.grasswort.picker.user.dto.UserRegisterResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;

/**
 * @author xuliangliang
 * @Classname UserRegisterServiceImpl
 * @Description 用户注册
 * @Date 2019/9/22 9:25
 * @blame Java Team
 */
@Slf4j
@Service
public class UserRegisterServiceImpl implements IUserRegisterService {

    @Override
    public UserRegisterResponse register(UserRegisterRequest request) {
        log.info("\n用户注册：{}", request.getUsername());
        UserRegisterResponse response = new UserRegisterResponse();
        response.setCode(SysRetCodeConstants.SUCCESS.getCode());
        response.setMsg(SysRetCodeConstants.SUCCESS.getMsg());
        log.info("\n注册成功：{}", request.getUsername());
        return response;
    }
}
