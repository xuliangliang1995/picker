package com.grasswort.picker.user.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.dubbo.common.URL;

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

    private static final String QRCODE_URL_PREFIX = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=";
    @JsonIgnore
    private String ticket;
    @JsonIgnore
    private String url;

    public String getQrcode() {
        return QRCODE_URL_PREFIX.concat(URL.encode(ticket));
    }
}
