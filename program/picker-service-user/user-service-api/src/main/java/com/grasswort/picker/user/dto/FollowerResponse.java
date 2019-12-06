package com.grasswort.picker.user.dto;

import com.grasswort.picker.user.dto.user.UserItem;
import lombok.Data;

import java.util.List;

/**
 * @author xuliangliang
 * @Classname FollowerResponse
 * @Description 粉丝列表
 * @Date 2019/12/6 14:35
 * @blame Java Team
 */
@Data
public class FollowerResponse extends AbstractUserResponse {

    private List<UserItem> followers;

    private Long total;

}
