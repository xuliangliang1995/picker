package com.grasswort.picker.user;

import com.grasswort.picker.user.dto.*;

/**
 * @author xuliangliang
 * @Classname IUserProfileService.java
 * @Description
 * @Date 2019/12/31
 * @blame Java Team
 */
public interface IUserProfileService {
    /**
     * 修改个人简介
     * @param introEditRequest
     * @return
     */
    UserIntroEditResponse editIntro(UserIntroEditRequest introEditRequest);

    /**
     * 编辑 github 链接
     * @return
     */
    EditGithubUrlResponse editGithubUrl(EditGithubUrlRequest editGithubUrlRequest);

    /**
     * 用户资料
     * @param profileRequest
     * @return
     */
    UserProfileResponse profile(UserProfileRequest profileRequest);

}
