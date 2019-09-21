package com.grasswort.picker.user.dto;

import com.grasswort.picker.commons.result.AbstractRequest;
import lombok.Data;

/**
 * @author xuliangliang
 * @Classname CheckAuthRequest
 * @Description TODO
 * @Date 2019/9/21 17:03
 * @blame Java Team
 */
@Data
public class CheckAuthRequest extends AbstractRequest {

    private String token;

    @Override
    public void requestCheck() {

    }
}
