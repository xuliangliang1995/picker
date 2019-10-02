package com.grasswort.picker.user.dto;

import lombok.Data;
import org.msgpack.annotation.Message;

/**
 * @author xuliangliang
 * @Classname JwtTokenUserClaim
 * @Description JwtToken 包含的用户信息
 * @Date 2019/10/2 10:10
 * @blame Java Team
 */
@Message
@Data
public class JwtTokenUserClaim {

    private Long id;

    private String name;
}
