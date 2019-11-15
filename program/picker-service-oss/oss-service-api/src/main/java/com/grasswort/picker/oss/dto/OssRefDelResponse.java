package com.grasswort.picker.oss.dto;

import com.grasswort.picker.commons.result.AbstractResponse;
import com.grasswort.picker.oss.constants.SysRetCodeConstants;
import lombok.Data;

/**
 * @author xuliangliang
 * @Classname OssRefDelResponse
 * @Description 删除引用
 * @Date 2019/10/17 23:26
 * @blame Java Team
 */
@Data
public class OssRefDelResponse extends AbstractResponse {

    @Override
    public boolean isSuccess() {
        return SysRetCodeConstants.SUCCESS.getCode().equals(this.getCode());
    }
}
