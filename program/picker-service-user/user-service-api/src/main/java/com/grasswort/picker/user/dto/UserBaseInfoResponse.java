package com.grasswort.picker.user.dto;

import com.grasswort.picker.commons.result.AbstractResponse;
import lombok.Data;

/**
 * @author xuliangliang
 * @Classname UserBaseInfoResponse
 * @Description 用户基本信息
 * @Date 2019/10/9 11:55
 * @blame Java Team
 */
@Data
public class UserBaseInfoResponse extends AbstractResponse {

    private String name;

    private String phone;

    private String email;

    private Byte sex;
}
