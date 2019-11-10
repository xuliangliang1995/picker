package com.grasswort.picker.blog;

import com.grasswort.picker.blog.dto.BlogCurveRequest;
import com.grasswort.picker.blog.dto.BlogCurveResponse;
import com.grasswort.picker.blog.dto.CurveListResponse;

/**
 * @author xuliangliang
 * @Classname IRetentionCurveService
 * @Description 记忆曲线
 * @Date 2019/11/10 12:04
 * @blame Java Team
 */
public interface IRetentionCurveService {
    /**
     * 获取记忆曲线
     * @return
     */
    CurveListResponse retentionCurve();

    /**
     * 博客记忆曲线修改
     * @return
     */
    BlogCurveResponse blogCurvePatch(BlogCurveRequest blogCurveRequest);


}
