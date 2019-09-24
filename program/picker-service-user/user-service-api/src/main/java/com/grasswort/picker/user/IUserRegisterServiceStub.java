package com.grasswort.picker.user;

import com.grasswort.picker.user.constants.SysRetCodeConstants;
import com.grasswort.picker.user.dto.UserRegisterRequest;
import com.grasswort.picker.user.dto.UserRegisterResponse;

/**
 * @author xuliangliang
 * @Classname UserRegisterServiceStub
 * @Description 注册桩
 * @Date 2019/9/24 20:05
 * @blame Java Team
 */
public class IUserRegisterServiceStub implements IUserRegisterService {

    private final IUserRegisterService iUserRegisterService;

    public IUserRegisterServiceStub(IUserRegisterService iUserRegisterService) {
        this.iUserRegisterService = iUserRegisterService;
    }

    @Override
    public UserRegisterResponse register(UserRegisterRequest request) {
        request.requestCheck();
        // 此代码在客户端执行
        try {
            return iUserRegisterService.register(request);
        } catch (Exception e) {
            UserRegisterResponse response = new UserRegisterResponse();
            response.setCode(SysRetCodeConstants.SYSTEM_TIMEOUT.getCode());
            response.setMsg(SysRetCodeConstants.SYSTEM_TIMEOUT.getMsg());
            return response;
        }
    }
}
