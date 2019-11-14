package com.grasswort.picker.wechat.dto;

import com.grasswort.picker.commons.result.AbstractResponse;
import lombok.Data;

/**
 * @author xuliangliang
 * @Classname WxQrcodeResponse
 * @Description 二维码
 * @Date 2019/11/14 19:21
 * @blame Java Team
 */
@Data
public class WxQrcodeResponse extends AbstractResponse {

    private String ticket;

    private String url;
    
}
