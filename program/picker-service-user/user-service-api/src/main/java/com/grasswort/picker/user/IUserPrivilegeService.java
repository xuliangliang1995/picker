package com.grasswort.picker.user;

import com.grasswort.picker.user.dto.UserPrivilegeRequest;
import com.grasswort.picker.user.dto.UserPrivilegeResponse;

/**
 * @author xuliangliang
 * @Classname IUserPrivilegeService
 * @Description 用户提高权限接口
 * @Date 2019/10/10 19:59
 * @blame Java Team
 */
public interface IUserPrivilegeService {
    /**
     * 提高用户操作权限
     * @param privilegeRequest
     * @return
     */
    UserPrivilegeResponse upgradePrivilege(UserPrivilegeRequest privilegeRequest);
}
