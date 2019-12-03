package com.grasswort.picker.blog.dto;

import com.grasswort.picker.commons.result.AbstractRequest;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author xuliangliang
 * @Classname HotWordRequest
 * @Description 热词
 * @Date 2019/12/3 14:49
 * @blame Java Team
 */
@Data
public class HotWordRequest extends AbstractRequest {
    @Min(1)
    @NotNull
    private Integer size;
    @Override
    public void requestCheck() {

    }

    public HotWordRequest() {
    }

    public HotWordRequest(@Min(1) @NotNull Integer size) {
        this.size = size;
    }
}
