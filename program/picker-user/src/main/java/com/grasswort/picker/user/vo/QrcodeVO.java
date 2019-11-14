package com.grasswort.picker.user.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xuliangliang
 * @Classname QrcodeVO
 * @Description 二维码信息
 * @Date 2019/11/14 20:12
 * @blame Java Team
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QrcodeVO {

    private String ticket;

    private String url;

    public String getQrcode() {
        return "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=".concat(ticket);
    }
}
