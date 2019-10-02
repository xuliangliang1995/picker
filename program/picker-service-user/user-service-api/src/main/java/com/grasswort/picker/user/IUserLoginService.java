package com.grasswort.picker.user;

import com.grasswort.picker.user.dto.CheckAuthRequest;
import com.grasswort.picker.user.dto.CheckAuthResponse;
import com.grasswort.picker.user.dto.UserLoginRequest;
import com.grasswort.picker.user.dto.UserLoginResponse;
import org.springframework.validation.annotation.Validated;

/**
 * @author xuliangliang
 * @Classname IUserLoginService
 * @Description 登录服务
 * @Date 2019/9/21 16:56
 * @blame Java Team
 */
public interface IUserLoginService {
    /**
     * 用户登录
     * @param request
     * @return
     */
    UserLoginResponse login(UserLoginRequest request);

    /**
     * 校验 token
     * @param request
     * @return
     */
    CheckAuthResponse validToken(@Validated CheckAuthRequest request);

}
