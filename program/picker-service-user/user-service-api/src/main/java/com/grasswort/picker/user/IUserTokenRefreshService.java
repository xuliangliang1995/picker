package com.grasswort.picker.user;

import com.grasswort.picker.user.dto.RefreshAccessTokenRequest;
import com.grasswort.picker.user.dto.RefreshAccessTokenResponse;

/**
 * @author xuliangliang
 * @Classname IUserTokenRefreshService
 * @Description 刷新 access_token 服务
 * @Date 2019/10/8 21:32
 * @blame Java Team
 */
public interface IUserTokenRefreshService {

    /**
     * refresh accessToken
     * @param refreshAccessTokenRequest
     * @return
     */
    RefreshAccessTokenResponse refreshAccessToken(RefreshAccessTokenRequest refreshAccessTokenRequest);
}
