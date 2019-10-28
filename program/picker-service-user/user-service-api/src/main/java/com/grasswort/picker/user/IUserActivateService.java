package com.grasswort.picker.user;

import com.grasswort.picker.user.dto.*;
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
     * 查看用户激活状态
     * @param request
     * @return
     */
    QueryActivateStatusResponse activateStatus(QueryActivateStatusRequest request);

    /**
     * 发送账户激活邮件
     * @param request
     * @return
     */
    SendActivateEmailResponse sendActivateEmail(@Validated SendActivateEmailRequest request);

    /**
     * 注册用户激活
     * @param request
     * @return
     */
    UserActivateResponse activate(@Validated UserActivateRequest request);


}
