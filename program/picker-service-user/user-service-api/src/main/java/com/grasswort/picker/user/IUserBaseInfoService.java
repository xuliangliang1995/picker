package com.grasswort.picker.user;

import com.grasswort.picker.user.dto.*;

/**
 * @author xuliangliang
 * @Classname IUserBaseInfoService
 * @Description 用户基本信息服务
 * @Date 2019/10/9 11:53
 * @blame Java Team
 */
public interface IUserBaseInfoService {
    /**
     * 获取用户基本信息
     * @param baseInfoRequest
     * @return
     */
    UserBaseInfoResponse userBaseInfo(UserBaseInfoRequest baseInfoRequest);

    /**
     * 编辑用户基本信息
     * @param editRequest
     * @return
     */
    UserBaseInfoEditResponse editUserBaseInfo(UserBaseInfoEditRequest editRequest);

    /**
     * 用户修改密码
     * 1.修改密码后该用户之前所有 token 都会失效,会返回新的 access_token 和 refresh_token
     * 2.修改密码为敏感操作，需要获取高权限 access_token
     * @param changePwdRequest
     * @return
     */
    UserChangePwdResponse changePwd(UserChangePwdRequest changePwdRequest);

    /**
     * 更换手机号（绑定手机号）
     * @param changePhoneRequest
     * @return
     */
    UserChangePhoneResponse changePhone(UserChangePhoneRequest changePhoneRequest);


}
