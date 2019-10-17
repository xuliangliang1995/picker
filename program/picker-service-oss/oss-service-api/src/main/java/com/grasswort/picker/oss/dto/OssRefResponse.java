package com.grasswort.picker.oss.dto;

import com.grasswort.picker.commons.result.AbstractResponse;
import lombok.Data;

/**
 * @author xuliangliang
 * @Classname OssRefResponse
 * @Description 提交引用响应
 * @Date 2019/10/17 21:24
 * @blame Java Team
 */
@Data
public class OssRefResponse extends AbstractResponse {
    /**
     * 引用的id
     */
    private Long id;
}
