package com.grasswort.picker.user.dto;

import lombok.Data;

/**
 * @author xuliangliang
 * @Classname PrivateMpQrcodeResponse
 * @Description 个人公众号二维码
 * @Date 2019/11/22 21:07
 * @blame Java Team
 */
@Data
public class PrivateMpQrcodeResponse extends AbstractUserResponse {
    /**
     * 二维码
     */
    private String qrcode;
}
