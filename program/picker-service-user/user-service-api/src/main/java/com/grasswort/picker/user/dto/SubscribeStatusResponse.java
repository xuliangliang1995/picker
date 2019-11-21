package com.grasswort.picker.user.dto;

import lombok.Data;

/**
 * @author xuliangliang
 * @Classname SubscribeStatusResponse
 * @Description 关注状态
 * @Date 2019/11/21 15:13
 * @blame Java Team
 */
@Data
public class SubscribeStatusResponse extends AbstractUserResponse {

    private boolean subscribe;

}
