package com.grasswort.picker.user.dto;

import com.grasswort.picker.user.constants.BlogPushMode;
import lombok.Data;

/**
 * @author xuliangliang
 * @Classname BlogPushSettingResponse
 * @Description 用户博客推送设置
 * @Date 2019/11/17 14:08
 * @blame Java Team
 */
@Data
public class BlogPushSettingResponse extends AbstractUserResponse {

    private Boolean openBlogPush;

    private BlogPushMode mode;

    private String openId;

    private String email;

}
