package com.grasswort.picker.user;

import com.grasswort.picker.user.dto.UserBaseInfoEditRequest;
import com.grasswort.picker.user.dto.UserBaseInfoEditResponse;
import com.grasswort.picker.user.dto.UserBaseInfoRequest;
import com.grasswort.picker.user.dto.UserBaseInfoResponse;

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
}
