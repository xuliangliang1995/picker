package com.grasswort.picker.user.dto;

import com.grasswort.picker.user.IUserLoginService;
import com.grasswort.picker.user.constants.SysRetCodeConstants;

/**
 * @author xuliangliang
 * @Classname IUserLoginServiceMock
 * @Description Mock
 * @Date 2019/10/2 15:58
 * @blame Java Team
 */
public class IUserLoginServiceMock implements IUserLoginService {
    @Override
    public UserLoginResponse login(UserLoginRequest request) {
        UserLoginResponse response = new UserLoginResponse();
        response.setCode(SysRetCodeConstants.SYSTEM_TIMEOUT.getCode());
        response.setMsg(SysRetCodeConstants.SYSTEM_TIMEOUT.getMsg());
        return response;
    }

    @Override
    public CheckAuthResponse validToken(CheckAuthRequest request) {
        CheckAuthResponse response = new CheckAuthResponse();
        response.setCode(SysRetCodeConstants.SYSTEM_TIMEOUT.getCode());
        response.setMsg(SysRetCodeConstants.SYSTEM_TIMEOUT.getMsg());
        return response;
    }
}
