package com.grasswort.picker.user;

import com.grasswort.picker.user.dto.UserRegisterRequest;
import com.grasswort.picker.user.dto.UserRegisterResponse;

/**
 * @author xuliangliang
 * @Classname IUserRegisterService
 * @Description TODO
 * @Date 2019/9/21 17:09
 * @blame Java Team
 */
public interface IUserRegisterService {

    /**
     * 实现用户注册功能
     * @param request
     * @return
     */
    UserRegisterResponse register(UserRegisterRequest request);
}
