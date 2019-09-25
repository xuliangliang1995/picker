package com.grasswort.picker.user;

import com.grasswort.picker.user.constants.SysRetCodeConstants;
import com.grasswort.picker.user.dto.UserRegisterRequest;
import com.grasswort.picker.user.dto.UserRegisterResponse;

/**
 * @author xuliangliang
 * @Classname IUserRegisterServiceMock
 * @Description mock
 * @Date 2019/9/25 23:13
 * @blame Java Team
 */
public class IUserRegisterServiceMock implements IUserRegisterService {
    @Override
    public UserRegisterResponse register(UserRegisterRequest request) {
        UserRegisterResponse response = new UserRegisterResponse();
        response.setCode(SysRetCodeConstants.USER_REGISTER_FAILED.getCode());
        response.setMsg(SysRetCodeConstants.USER_REGISTER_FAILED.getMsg());
        return response;
    }
}
