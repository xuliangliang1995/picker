package com.grasswort.picker.user.dto;

import lombok.Data;
import org.msgpack.annotation.Message;

/**
 * @author xuliangliang
 * @Classname JwtRefreshTokenUserClaim
 * @Description access_token 信息
 * @Date 2019/10/8 19:50
 * @blame Java Team
 */
@Data
@Message
public class JwtRefreshTokenUserClaim {
    /**
     * 用户 ID
     */
    private Long id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 用户版本号，当用户修改密码后，会提升版本，旧版本会失效
     */
    private Integer version;

}
