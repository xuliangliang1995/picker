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
public class JwtAccessTokenUserClaim {
    /**
     * 用户 ID
     */
    private Long id;
    /**
     * 用户姓名
     */
    private String name;
    /**
     * 版本
     */
    private Integer version;
}
