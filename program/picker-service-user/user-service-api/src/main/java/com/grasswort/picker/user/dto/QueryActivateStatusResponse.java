package com.grasswort.picker.user.dto;

import com.grasswort.picker.commons.result.AbstractResponse;
import com.grasswort.picker.user.constants.SysRetCodeConstants;
import lombok.Data;

/**
 * @author xuliangliang
 * @Classname QueryActivateStatusResponse
 * @Description 激活状态
 * @Date 2019/10/15 16:38
 * @blame Java Team
 */
@Data
public class QueryActivateStatusResponse extends AbstractResponse {
    /**
     * 是否激活
     */
    private boolean activated;
    /**
     * 如果没有激活，返回激活邮箱，如果邮箱也没有，说明账号不存在
     */
    private String email;

    @Override
    public boolean isSuccess() {
        return SysRetCodeConstants.SUCCESS.getCode().equals(this.getCode());
    }

}
