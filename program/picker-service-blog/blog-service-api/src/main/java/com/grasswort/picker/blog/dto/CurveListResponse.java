package com.grasswort.picker.blog.dto;

import com.grasswort.picker.blog.constant.SysRetCodeConstants;
import com.grasswort.picker.blog.dto.curve.RetentionCurveItem;
import com.grasswort.picker.commons.result.AbstractResponse;
import lombok.Data;

import java.util.List;

/**
 * @author xuliangliang
 * @Classname CurveListResponse
 * @Description 记忆曲线
 * @Date 2019/11/10 12:08
 * @blame Java Team
 */
@Data
public class CurveListResponse extends AbstractResponse {

    private List<RetentionCurveItem> curves;

    @Override
    public boolean isSuccess() {
        return SysRetCodeConstants.SUCCESS.getCode().equals(this.getCode());
    }

}
