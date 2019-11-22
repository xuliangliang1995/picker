package com.grasswort.picker.user;

import com.grasswort.picker.user.dto.PrivateMpQrcodeRequest;
import com.grasswort.picker.user.dto.PrivateMpQrcodeResponse;
import com.grasswort.picker.user.dto.UploadPrivateMpQrcodeRequest;
import com.grasswort.picker.user.dto.UploadPrivateMpQrcodeResponse;
import org.springframework.validation.annotation.Validated;

/**
 * @author xuliangliang
 * @Classname IUserPrivateMpQrcodeService
 * @Description 个人公众号推广服务
 * @Date 2019/11/22 21:03
 * @blame Java Team
 */
public interface IUserPrivateMpQrcodeService {
    /**
     * 上传个人微信公众号二维码
     * @param uploadPrivateMpQrcodeRequest
     * @return
     */
    UploadPrivateMpQrcodeResponse uploadPrivateMpQrcode(@Validated UploadPrivateMpQrcodeRequest uploadPrivateMpQrcodeRequest);

    /**
     * 获取个人公众号二维码
     * @param privateMpQrcodeRequest
     * @return
     */
    PrivateMpQrcodeResponse getPrivateMpQrcode(PrivateMpQrcodeRequest privateMpQrcodeRequest);
}
