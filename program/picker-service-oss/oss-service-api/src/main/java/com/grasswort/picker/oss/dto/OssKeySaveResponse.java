package com.grasswort.picker.oss.dto;

import com.grasswort.picker.commons.result.AbstractResponse;
import com.grasswort.picker.oss.constants.SysRetCodeConstants;
import lombok.Data;

/**
 * @author xuliangliang
 * @Classname OssKeySaveResponse
 * @Description Oss key 存储
 * @Date 2019/10/17 22:50
 * @blame Java Team
 */
@Data
public class OssKeySaveResponse extends AbstractResponse {

    @Override
    public boolean isSuccess() {
        return SysRetCodeConstants.SUCCESS.getCode().equals(this.getCode());
    }
}
