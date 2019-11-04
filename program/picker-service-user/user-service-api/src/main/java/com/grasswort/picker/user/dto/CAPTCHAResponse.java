package com.grasswort.picker.user.dto;

import com.grasswort.picker.commons.result.AbstractResponse;
import lombok.Data;

/**
 * @author xuliangliang
 * @Classname CAPTCHAResponse
 * @Description
 * @Date 2019/10/8 23:09
 * @blame Java Team
 */
@Data
public class CAPTCHAResponse extends AbstractResponse {
    /**
     * 发送的邮箱地址
     */
    private String email;

    /**
     * 发送的手机号码
     */
    private String phone;

}
