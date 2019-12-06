package com.grasswort.picker.user.dto;

import com.grasswort.picker.user.dto.user.UserItem;
import lombok.Data;

import java.util.List;

/**
 * @author xuliangliang
 * @Classname FollowingResponse
 * @Description 关注列表
 * @Date 2019/12/6 14:38
 * @blame Java Team
 */
@Data
public class FollowingResponse extends AbstractUserResponse {

    private List<UserItem> users;

    private Long total;

}
