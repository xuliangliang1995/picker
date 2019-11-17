package com.grasswort.picker.user.dto;

import com.grasswort.picker.commons.result.AbstractResponse;
import com.grasswort.picker.user.constants.SysRetCodeConstants;

/**
 * @author xuliangliang
 * @Classname AbstractUserResponse
 * @Description 抽象用户返回
 * @Date 2019/11/17 14:09
 * @blame Java Team
 */
public abstract class AbstractUserResponse extends AbstractResponse {
    @Override
    public boolean isSuccess() {
        return SysRetCodeConstants.SUCCESS.getCode().equals(this.getCode());
    }
}
