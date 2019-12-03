package com.grasswort.picker.user.dto;

import com.grasswort.picker.user.dto.user.UserItem;
import lombok.Data;

import java.util.List;

/**
 * @author xuliangliang
 * @Classname UserSearchResponse
 * @Description 用户查询结果
 * @Date 2019/12/3 23:03
 * @blame Java Team
 */
@Data
public class UserSearchResponse extends AbstractUserResponse {

    private List<UserItem> users;

    private long total;
}
