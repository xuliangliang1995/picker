package com.grasswort.picker.user;

import com.grasswort.picker.user.dto.UserActivateRequest;
import com.grasswort.picker.user.dto.UserActivateResponse;
import org.springframework.validation.annotation.Validated;

/**
 * @author xuliangliang
 * @Classname IUserActivateService
 * @Description 用户激活服务
 * @Date 2019/10/6 22:38
 * @blame Java Team
 */
public interface IUserActivateService {
    /**
     * 注册用户激活
     * @param request
     * @return
     */
    UserActivateResponse activate(@Validated UserActivateRequest request);
}
