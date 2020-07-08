package com.grasswort.picker.user;

import com.grasswort.picker.user.dto.RefreshUserPoolRequest;
import com.grasswort.picker.user.dto.UserSearchRequest;
import com.grasswort.picker.user.dto.UserSearchResponse;

/**
 * @author xuliangliang
 * @Classname IUserSearchService
 * @Description 用户查询服务
 * @Date 2019/12/3 23:01
 * @blame Java Team
 */
public interface IUserSearchService {

    /**
     * 用户查询
     * @param userSearchRequest
     * @return
     */
    UserSearchResponse search(UserSearchRequest userSearchRequest);

    /**
     * 刷新用户池
     * @param refreshUserPoolRequest
     */
    void refreshUserPool(RefreshUserPoolRequest refreshUserPoolRequest);
}
