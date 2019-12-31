package com.grasswort.picker.user.dto;

import lombok.Data;

/**
 * @author xuliangliang
 * @Classname UserProfileResponse.java
 * @Description
 * @Date 2019/12/31
 * @blame Java Team
 */
@Data
public class UserProfileResponse extends AbstractUserResponse {

    private String intro;

    private String github;
}
